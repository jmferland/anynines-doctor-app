package org.springsource.cloudfoundry.mvc.services;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CustomerServiceTest extends BaseServiceTest {
    @Autowired
    private CustomerService customerService;

    Date signupDate = new Date();

    String firstName = "Josh";

    String lastName = "Long";

    @Test
    public void testCreatingCustomers() {
        Customer customer = customerService.createCustomer(this.firstName, this.lastName, this.signupDate);
        assertNotNull("the customer can't be null", customer);
        assertEquals(customer.getFirstName(), this.firstName);
        assertEquals(customer.getLastName(), this.lastName);
        assertEquals(customer.getSignupDate(), this.signupDate);
    }
    
    @Test
    public void testGetAllCustomers() {
        customerService.createCustomer(this.firstName, this.lastName, this.signupDate);
        assertEquals(1, customerService.getAllCustomers().size());
    }

    @Test
    public void testUpdatingACustomer() throws Exception {
        Customer customer = customerService.createCustomer(this.firstName, this.lastName, this.signupDate);
        assertNotNull("the customer can't be null", customer);
        assertEquals(customer.getFirstName(), this.firstName);
        assertEquals(customer.getLastName(), this.lastName);
        assertEquals(customer.getSignupDate(), this.signupDate);

        customerService.updateCustomer(customer.getId(), "Joshua", customer.getLastName(), customer.getSignupDate());

        customer = customerService.getCustomerById(customer.getId());
        assertEquals(customer.getFirstName(), "Joshua");

    }

    @Test
    public void testSearchingForCustomers() throws Exception {
        customerService.createCustomer(this.firstName, this.lastName, this.signupDate);
        assertEquals(1, customerService.search("josh").size());
    }
}
