package org.springsource.cloudfoundry.mvc.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Collection;

import org.junit.Test;

public class RegistrationServiceTest extends BaseServiceTest {
    
    @Test
    public void testSearch_Success() {
    	Merchant merchant = createMerchant("asdf");
        
        assertSearchResult(merchant.getName(), merchant);
    }
    
    private void assertSearchResult(String query, Merchant merchant) {
    	Collection<Merchant> results = merchantService.search(query);
        assertThat(results.size(), is(1));
        assertThat(results.iterator().next().getId(), is(merchant.getId()));
    }
}
