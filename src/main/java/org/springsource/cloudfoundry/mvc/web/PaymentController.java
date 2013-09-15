package org.springsource.cloudfoundry.mvc.web;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springsource.cloudfoundry.mvc.services.Bill;
import org.springsource.cloudfoundry.mvc.services.BillService;
import org.springsource.cloudfoundry.mvc.services.Merchant;
import org.springsource.cloudfoundry.mvc.services.Registration;
import org.springsource.cloudfoundry.mvc.services.RegistrationService;

import com.jayway.jsonpath.JsonPath;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

@Controller
public class PaymentController {
    private Logger log = Logger.getLogger(getClass());
    
    private static final String GENERATE_TOKEN_URL = "https://test.ctpe.net/frontend/GenerateToken";
    private static final String GET_STATUS_URL = "https://test.ctpe.net/frontend/GetStatus;jsessionid=";

    @Autowired  private BillService billService;
    @Autowired  private RegistrationService registrationService;

    public static final String PAYMENT_URL = "/pay/{billToken}";
    public static final String PAYMENT_COMPLETE_URL = "/thanks/{billToken}";

    @RequestMapping(value = PAYMENT_URL, method = RequestMethod.GET)
    public ModelAndView pay(@PathVariable String billToken) throws Exception {
    	Bill bill = billService.getBillByToken(billToken);
    	Collection<Registration> registrations = registrationService.getRegistrationsByCustomerId(bill.getCustomer().getId());
    	String token = generateToken(bill, null);
    	
    	Map<String, Registration> tokenToRegistration = generateTokens(bill, registrations);
    	
    	ModelAndView modelAndView = new ModelAndView("pay");
    	modelAndView.addObject("bill", bill);
    	modelAndView.addObject("token", token);
    	modelAndView.addObject("tokenToRegistration", tokenToRegistration);
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = PAYMENT_COMPLETE_URL, method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView thanks(@PathVariable String billToken, @RequestParam String token) {
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
        return modelAndView;
    }
    
    private Map<String, Registration> generateTokens(Bill bill, Collection<Registration> registrations) {
    	Map<String, Registration> tokenToRegistration = new HashMap<String, Registration>();
    	
    	for (Registration registration : registrations) {
    		try {
    			String token = generateToken(bill, registration);
    			tokenToRegistration.put(token, registration);
    		} catch (Exception e) {
    			log.error("Exception while generating token for registration id " + registration.getId(), e);
    		}
    	}
    	
    	return tokenToRegistration;
    }
    
    private void addRegistration(String status, String billToken) {
    	try {
    		String brand = JsonPath.read(status, "$.transaction.account.brand");
    		String bin = JsonPath.read(status, "$.transaction.account.bin");
    		String last4Digits = JsonPath.read(status, "$.transaction.account.last4Digits");
    		String registrationCode = JsonPath.read(status, "$.transaction.account.registration"); // might not exist if paid with RG
    		
        	Bill bill = billService.getBillByToken(billToken);
    		registrationService.createRegistration(bill.getCustomer(), registrationCode, brand, bin, last4Digits);
    	} catch (Exception e) {} // if we can't get info to add, don't add it! If it's already added, who cares!
    }
    
    private String generateToken(Bill bill, Registration registration) throws Exception {
    	try {
	    	URL url = new URL(GENERATE_TOKEN_URL);
	    	HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
	    	 
	    	conn.setRequestMethod("POST");
	    	conn.setDoInput(true);
	    	conn.setDoOutput(true);
	    	
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
    
    private String getStatus(String token) throws Exception {
    	URL url = new URL(GET_STATUS_URL + token);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
         
        String content = IOUtils.toString(conn.getInputStream());
        return content;
    }
}