package org.springsource.cloudfoundry.mvc.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Collection;

import org.junit.Test;

public class MerchantServiceTest extends BaseServiceTest {
    
    @Test
    public void testSearch_Success() {
    	Customer customer = createCustomer();
    	Registration registration = createRegistration(customer);
        
        assertSearchResult(registration.getCustomer().getFirstName(), registration);
        assertSearchResult(registration.getBrand(), registration);
        assertSearchResult(registration.getBin(), registration);
        assertSearchResult(registration.getCode(), registration);
    }
    
    private void assertSearchResult(String query, Registration registration) {
    	Collection<Registration> results = registrationService.search(query);
        assertThat(results.size(), is(1));
        assertThat(results.iterator().next().getId(), is(registration.getId()));
    }
}
