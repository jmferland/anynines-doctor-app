package org.springsource.cloudfoundry.mvc.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springsource.cloudfoundry.mvc.services.Expiry;
import org.springsource.cloudfoundry.mvc.services.Registration;
import org.springsource.cloudfoundry.mvc.services.RegistrationService;
import org.springsource.cloudfoundry.mvc.services.Customer;
import org.springsource.cloudfoundry.mvc.services.CustomerService;
import org.springsource.cloudfoundry.mvc.services.MerchantService;

import java.util.Collection;
import java.util.List;

@Controller
public class RegistrationApiController {
    private Logger log = Logger.getLogger(getClass());

    @Autowired  private RegistrationService registrationService;
    @Autowired  private MerchantService merchantService;
    @Autowired  private CustomerService customerService;

    public static final String REGISTRATIONS_ENTRY_URL = "/crm/registrations";
    public static final String REGISTRATIONS_SEARCH_URL = "/crm/search/registrations";
    public static final String REGISTRATIONS_BY_ID_ENTRY_URL = REGISTRATIONS_ENTRY_URL + "/{id}";

    @ResponseBody
    @RequestMapping(value = REGISTRATIONS_SEARCH_URL, method = RequestMethod.GET)
    public Collection<Registration> search(@RequestParam("q") String query) throws Exception {
        Collection<Registration> registrations = registrationService.search(query);
        if (log.isDebugEnabled())
            log.debug(String.format("retrieved %s results for search query '%s'", Integer.toString(registrations.size()), query));
        return registrations;
    }

    @ResponseBody
    @RequestMapping(value = REGISTRATIONS_BY_ID_ENTRY_URL, method = RequestMethod.GET)
    public Registration registrationById(@PathVariable  Integer id) {
        return this.registrationService.getRegistrationById(id);
    }

    @ResponseBody
    @RequestMapping(value = REGISTRATIONS_ENTRY_URL, method = RequestMethod.GET)
    public List<Registration> registrations() {
        return this.registrationService.getAllRegistrations();
    }

    @ResponseBody
    @RequestMapping(value = REGISTRATIONS_ENTRY_URL, method = RequestMethod.PUT)
    public Integer addRegistration(@RequestParam String customerId,
    		@RequestParam String code,
    		@RequestParam String brand,
    		@RequestParam String bin,
    		@RequestParam String last4Digits,
    		@RequestParam String expiryYear,
    		@RequestParam String expiryMonth) {
    	Expiry expiry = new Expiry().setMonth(expiryMonth).setYear(expiryYear);
    	Customer customer = customerService.getCustomerById(Integer.valueOf(customerId));
        return registrationService.createRegistration(customer, code, brand, bin, last4Digits, expiry).getId();
    }

    @ResponseBody
    @RequestMapping(value = REGISTRATIONS_BY_ID_ENTRY_URL, method = RequestMethod.POST)
    public Integer updateRegistration(@PathVariable  Integer id, 
    		@RequestParam String customerId,
    		@RequestParam String code,
    		@RequestParam String brand,
    		@RequestParam String bin,
    		@RequestParam String last4Digits,
    		@RequestParam String expiryYear,
    		@RequestParam String expiryMonth) {
    	Expiry expiry = new Expiry().setMonth(expiryMonth).setYear(expiryYear);
    	Customer customer = customerService.getCustomerById(Integer.valueOf(customerId));
        registrationService.updateRegistration(id, customer, code, brand, bin, last4Digits, expiry);
        return id;
    }


    @ResponseBody
    @RequestMapping(value = REGISTRATIONS_BY_ID_ENTRY_URL, method = RequestMethod.DELETE)
    public void deleteRegistrationById(@PathVariable  Integer id) {
        this.registrationService.deleteRegistration(id);
    }
}