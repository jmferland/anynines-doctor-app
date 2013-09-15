package org.springsource.cloudfoundry.mvc.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springsource.cloudfoundry.mvc.services.Customer;
import org.springsource.cloudfoundry.mvc.services.CustomerService;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Controller
public class PaymentController {
    private Logger log = Logger.getLogger(getClass());

    @Autowired  private CustomerService customerService;

    public static final String PAYMENT_URL = "/pay/{billToken}";
    public static final String PAYMENT_COMPLETE_URL = "/thanks/{billToken}";

    @RequestMapping(value = PAYMENT_URL, method = RequestMethod.GET)
    public ModelAndView pay(@PathVariable String billToken) throws Exception {
    	// TODO: generate the token, load the relevant registrations, then forward to the jsp view
    	// to offer payment with a new CC or with a registration
    	ModelAndView modelAndView = null;
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = PAYMENT_COMPLETE_URL, method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView thanks(@PathVariable String billToken, @RequestParam String token) {
    	// TODO: get the status, if was successful, then add the registration if it doesn't already exist
    	ModelAndView modelAndView = null;
        return modelAndView;
    }
}