package org.springsource.cloudfoundry.mvc.web;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.cloudfoundry.runtime.env.CloudEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springsource.cloudfoundry.mvc.model.RegistrationToken;
import org.springsource.cloudfoundry.mvc.services.Bill;
import org.springsource.cloudfoundry.mvc.services.BillService;
import org.springsource.cloudfoundry.mvc.services.Expiry;
import org.springsource.cloudfoundry.mvc.services.Merchant;
import org.springsource.cloudfoundry.mvc.services.Registration;
import org.springsource.cloudfoundry.mvc.services.RegistrationService;
import org.springsource.cloudfoundry.mvc.web.ssl.TrustAllTrustManager;

import com.jayway.jsonpath.JsonPath;

import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

@Controller
public class PaymentController {
    private Logger log = Logger.getLogger(getClass());
    
    private static final String GENERATE_TOKEN_URL = "https://test.ctpe.net/frontend/GenerateToken";
    private static final String GET_STATUS_URL = "https://test.ctpe.net/frontend/GetStatus;jsessionid=";
    private static AtomicReference<Boolean> sslSocketFactoryInitialized = new AtomicReference<Boolean>(false);

    @Autowired  private BillService billService;
    @Autowired  private RegistrationService registrationService;

    public static final String PAYMENT_URL = "/pay/{billToken}";
    public static final String THANKS_URL = "/thanks/{billToken}";

    @RequestMapping(value = PAYMENT_URL, method = RequestMethod.GET)
    public ModelAndView pay(WebRequest request, @PathVariable String billToken) throws Exception {
    	Bill bill = billService.getBillByToken(billToken);
    	Collection<Registration> registrations = registrationService.getRegistrationsByCustomerId(bill.getCustomer().getId());
    	String token = generateToken(bill, null);
    	
    	ModelAndView modelAndView = new ModelAndView("pay");
    	modelAndView.addObject("bill", bill);
    	modelAndView.addObject("token", token);
    	modelAndView.addObject("redirectUrl", getRedirectUrl(request, bill));
    	modelAndView.addObject("registrationTokens", prepareRegistrations(bill, registrations));
        return modelAndView;
    }
    
    private String getRedirectUrl(WebRequest request, Bill bill) {
    	String domain = new CloudEnvironment().getInstanceInfo().getUris().get(0);
    	String context = request.getContextPath();
    	String redirectUrl = "https://" + domain + context + THANKS_URL;
    	return redirectUrl.replace("{billToken}", bill.getToken());
    }

    @ResponseBody
    @RequestMapping(value = THANKS_URL, method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView thanks(@PathVariable String billToken, @RequestParam String token) {
    	Bill bill = billService.getBillByToken(billToken);
    	boolean success = false;
    	try {
    		String status = getStatus(token);
    		success = "ACK".equals(JsonPath.read(status, "$.transaction.processing.result"));
    		addRegistration(status, billToken);
    	} catch (Exception e) {
    		log.error("Exception while getting status", e);
    	}
    	
    	ModelAndView modelAndView = new ModelAndView("thanks");
    	modelAndView.addObject("success", success);
    	modelAndView.addObject("billToken", billToken);
    	modelAndView.addObject("bill", bill);
        return modelAndView;
    }
    
    private Set<RegistrationToken> prepareRegistrations(Bill bill, Collection<Registration> registrations) {
    	Set<RegistrationToken> preparedRegistrations = new TreeSet<RegistrationToken>();
    	
    	for (Registration registration : registrations) {
    		try {
    			String token = generateToken(bill, registration);
    			preparedRegistrations.add(new RegistrationToken(registration, token));
    		} catch (Exception e) {
    			log.error("Exception while generating token for registration id " + registration.getId(), e);
    		}
    	}
    	
    	return preparedRegistrations;
    }
    
    private void addRegistration(final String status, String billToken) {
    	try {
    		String registrationCode = JsonPath.read(status, "$.transaction.account.registration"); // might not exist if paid with RG
    		String brand = JsonPath.read(status, "$.transaction.account.brand");
    		String bin = JsonPath.read(status, "$.transaction.account.bin");
    		String last4Digits = JsonPath.read(status, "$.transaction.account.last4Digits");
    		Expiry expiry = getExpiry(status);
    		
        	Bill bill = billService.getBillByToken(billToken);
    		registrationService.createRegistration(bill.getCustomer(), registrationCode, brand, bin, last4Digits, expiry);
    	} catch (Exception e) {} // if we can't get info to add, don't add it! If it's already added, who cares!
    }
    
    private Expiry getExpiry(final String status) {
    	Expiry expiry = new Expiry();
    	String expiryYear = new Attempt<String>() { @Override String to() { return JsonPath.read(status, "$.transaction.account.expiry.year"); }}.get();
		String expiryMonth = new Attempt<String>() { @Override String to() { return JsonPath.read(status, "$.transaction.account.expiry.month"); }}.get();;
		expiry.setMonth(expiryMonth);
		expiry.setYear(expiryYear);
		return expiry;
    }
    
    public abstract static class Attempt<T> {
    	abstract T to();
    	
    	public T get() {
    		return get(null);
    	}
    	
    	public T get(T defaultValue) {
    		try {
    			return to();
    		} catch (Exception e) {}
    		return defaultValue;
    	}
    }
    
    private String generateToken(Bill bill, Registration registration) throws Exception {
    	try {
	    	HttpsURLConnection conn = getConnection(GENERATE_TOKEN_URL);
	    	
	    	Merchant merchant = bill.getMerchant();
	    	 
	    	String parameters = "SECURITY.SENDER=" + merchant.getSecuritySender()
	    	    + "&TRANSACTION.CHANNEL=" + merchant.getChannelId()
	    	    + "&TRANSACTION.MODE=INTEGRATOR_TEST"
	    	    + "&USER.LOGIN=" + merchant.getUserLogin()
	    	    + "&USER.PWD=" + merchant.getUserPassword()
	    	    + "&PRESENTATION.AMOUNT=" + bill.getAmount()
	    	    + "&PRESENTATION.CURRENCY=" + bill.getCurrency();
	    	
	    	if (registration == null) {
	    		parameters += "&PAYMENT.TYPE=RG.DB";
	    	} else {
	    		parameters += "&PAYMENT.TYPE=DB"
	    			+ "&ACCOUNT.REGISTRATION=" + registration.getCode();
	    	}
	    	
	    	log.info("Sending parameters: " + parameters);
	    	 
	    	IOUtils.write(parameters, conn.getOutputStream());
	    	 
	    	conn.connect();
	    	 
	    	String content = IOUtils.toString(conn.getInputStream());
	    	String token = JsonPath.read(content, "$.transaction.token");
	    	
	    	log.info("Got token: " + token);
	    	
	    	return token;
	    	
    	} catch (Exception e) {
    		log.error("Exception while generating token", e);
    		throw e;
    	}
    }
    
    private static void initSslSocketFactory() throws IOException, NoSuchAlgorithmException, KeyManagementException {
    	if (!sslSocketFactoryInitialized.getAndSet(true)) {
	    	SSLContext sslContext = SSLContext.getInstance("SSL");
	    	sslContext.init(null, new TrustManager[] { new TrustAllTrustManager() }, new SecureRandom());
	    	HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
    	}
    }
    
    private HttpsURLConnection getConnection(String urlString) throws IOException, NoSuchAlgorithmException, KeyManagementException {
    	initSslSocketFactory();
    	
    	URL url = new URL(urlString);
    	HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
    	 
    	conn.setRequestMethod("POST");
    	conn.setDoInput(true);
    	conn.setDoOutput(true);
    	
    	return conn;
    }
    
    private String getStatus(String token) throws Exception {
    	String url = GET_STATUS_URL + token;
        HttpsURLConnection conn = getConnection(url);
         
        String content = IOUtils.toString(conn.getInputStream());
        return content;
    }
}