package org.springsource.cloudfoundry.mvc.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springsource.cloudfoundry.mvc.services.Bill;
import org.springsource.cloudfoundry.mvc.services.BillService;
import org.springsource.cloudfoundry.mvc.services.Customer;
import org.springsource.cloudfoundry.mvc.services.CustomerService;
import org.springsource.cloudfoundry.mvc.services.Merchant;
import org.springsource.cloudfoundry.mvc.services.MerchantService;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Controller
public class BillApiController {
    private Logger log = Logger.getLogger(getClass());

    @Autowired  private BillService billService;
    @Autowired  private MerchantService merchantService;
    @Autowired  private CustomerService customerService;

    public static final String BILLS_ENTRY_URL = "/crm/bills";
    public static final String BILLS_SEARCH_URL = "/crm/search/bills";
    public static final String BILLS_BY_ID_ENTRY_URL = BILLS_ENTRY_URL + "/{id}";

    @ResponseBody
    @RequestMapping(value = BILLS_SEARCH_URL, method = RequestMethod.GET)
    public Collection<Bill> search(@RequestParam("q") String query) throws Exception {
        Collection<Bill> bills = billService.search(query);
        if (log.isDebugEnabled())
            log.debug(String.format("retrieved %s results for search query '%s'", Integer.toString(bills.size()), query));
        return bills;
    }

    @ResponseBody
    @RequestMapping(value = BILLS_BY_ID_ENTRY_URL, method = RequestMethod.GET)
    public Bill billById(@PathVariable  Integer id) {
        return this.billService.getBillById(id);
    }

    @ResponseBody
    @RequestMapping(value = BILLS_ENTRY_URL, method = RequestMethod.GET)
    public List<Bill> bills() {
        return this.billService.getAllBills();
    }

    @ResponseBody
    @RequestMapping(value = BILLS_ENTRY_URL, method = RequestMethod.PUT)
    public Integer addBill(@RequestParam String merchantId, @RequestParam String customerId,
    		@RequestParam String amount, @RequestParam String currency) {
    	Merchant merchant = merchantService.getMerchantById(Integer.valueOf(merchantId));
    	Customer customer = customerService.getCustomerById(Integer.valueOf(customerId));
    	BigDecimal bdAmount = new BigDecimal(amount);
        return billService.createBill(merchant, customer, bdAmount, currency).getId();
    }

    @ResponseBody
    @RequestMapping(value = BILLS_BY_ID_ENTRY_URL, method = RequestMethod.POST)
    public Integer updateBill(@PathVariable  Integer id, @RequestParam String merchantId,
    		@RequestParam String customerId, @RequestParam String amount,
    		@RequestParam String currency) {
    	Merchant merchant = merchantService.getMerchantById(Integer.valueOf(merchantId));
    	Customer customer = customerService.getCustomerById(Integer.valueOf(customerId));
    	BigDecimal bdAmount = new BigDecimal(amount);
        billService.updateBill(id, merchant, customer, bdAmount, currency);
        return id;
    }
}