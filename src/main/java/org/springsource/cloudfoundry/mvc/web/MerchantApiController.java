package org.springsource.cloudfoundry.mvc.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springsource.cloudfoundry.mvc.services.Merchant;
import org.springsource.cloudfoundry.mvc.services.MerchantService;

import java.util.Collection;
import java.util.List;

@Controller
public class MerchantApiController {
    private Logger log = Logger.getLogger(getClass());

    @Autowired  private MerchantService merchantService;

    public static final String MERCHANTS_ENTRY_URL = "/crm/merchants";
    public static final String MERCHANTS_SEARCH_URL = "/crm/search/merchants";
    public static final String MERCHANTS_BY_ID_ENTRY_URL = MERCHANTS_ENTRY_URL + "/{id}";

    @ResponseBody
    @RequestMapping(value = MERCHANTS_SEARCH_URL, method = RequestMethod.GET)
    public Collection<Merchant> search(@RequestParam("q") String query) throws Exception {
        Collection<Merchant> merchants = merchantService.search(query);
        if (log.isDebugEnabled())
            log.debug(String.format("retrieved %s results for search query '%s'", Integer.toString(merchants.size()), query));
        return merchants;
    }

    @ResponseBody
    @RequestMapping(value = MERCHANTS_BY_ID_ENTRY_URL, method = RequestMethod.GET)
    public Merchant merchantById(@PathVariable  Integer id) {
        return this.merchantService.getMerchantById(id);
    }

    @ResponseBody
    @RequestMapping(value = MERCHANTS_ENTRY_URL, method = RequestMethod.GET)
    public List<Merchant> merchants() {
        return this.merchantService.getAllMerchants();
    }

    @ResponseBody
    @RequestMapping(value = MERCHANTS_ENTRY_URL, method = RequestMethod.PUT)
    public Integer addMerchant(@RequestParam String name, @RequestParam String securitySender,
    		 @RequestParam String userLogin, @RequestParam String userPassword,
    		 @RequestParam String channelId) {
        return merchantService.createMerchant(name,
        		securitySender,
        		userLogin,
        		userPassword,
        		channelId).getId();
    }

    @ResponseBody
    @RequestMapping(value = MERCHANTS_BY_ID_ENTRY_URL, method = RequestMethod.POST)
    public Integer updateMerchant(@PathVariable  Integer id, @RequestBody Merchant merchant) {
    	merchantService.updateMerchant(id,
    			merchant.getName(),
    			merchant.getSecuritySender(),
    			merchant.getUserLogin(),
    			merchant.getUserPassword(),
    			merchant.getChannelId());
        return id;
    }
}