package org.springsource.cloudfoundry.mvc.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.math.BigDecimal;
import java.util.Date;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springsource.cloudfoundry.mvc.services.config.ServicesConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("default")
@ContextConfiguration
public class BaseServiceTest {
    @Autowired
    protected MerchantService merchantService;
    
    @Autowired
    protected BillService billService;
    
    @Autowired
    protected RegistrationService registrationService;
    
    @Autowired
    protected CustomerService customerService;
    
	JdbcTemplate jdbcTemplate;
    
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private DataSource dataSource;
	
    @Configuration
    @Import({ServicesConfiguration.class})
    public static class ServiceTestConfiguration {
        // noop we just want the beans in the ServicesConfiguration class
    }
    
    @Before
    public void before() throws Exception {

        jdbcTemplate = new JdbcTemplate(dataSource);

        TransactionTemplate transactionTemplate = new TransactionTemplate(this.transactionManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                jdbcTemplate.execute("delete from BILL");
                jdbcTemplate.execute("delete from REGISTRATION");
                jdbcTemplate.execute("delete from CUSTOMER");
                jdbcTemplate.execute("delete from MERCHANT");
            }
        });
    }
    
    @Test
    public void noop() {
    	// so surefire doesn't complain about no test in this class
    }
    
    protected Bill createBill(Merchant merchant)
    {
    	Bill bill = billService.createBill(
    			merchant,
    			new BigDecimal("1.00"),
    			"EUR");
    	
    	Bill actual = billService.getBillById(bill.getId());
        
        assertThat(actual, is(not(nullValue())));
        assertThat(actual.getMerchant().getName(), is(bill.getMerchant().getName()));
        assertThat(actual.getAmount(), is(bill.getAmount()));
        assertThat(actual.getCurrency(), is(bill.getCurrency()));
    	
    	return bill;
    }
    
    protected Merchant createMerchant(String name) {
    	Merchant merchant = merchantService.createMerchant(
    			name,
    			"696a8f0fabffea91517d0eb0a0bf9c33",
    			"1143238d620a572a726fe92eede0d1ab",
    			"demo",
    			"52275ebaf361f20a76b038ba4c806991");
    	
    	Merchant actual = merchantService.getMerchantById(merchant.getId());
        
        assertThat(actual, is(not(nullValue())));
        assertThat(actual.getName(), is(merchant.getName()));
        assertThat(actual.getSecuritySender(), is(merchant.getSecuritySender()));
        assertThat(actual.getUserLogin(), is(merchant.getUserLogin()));
        assertThat(actual.getUserPassword(), is(merchant.getUserPassword()));
        assertThat(actual.getChannelId(), is(merchant.getChannelId()));
    	
    	return merchant;
    }
    
    protected Customer createCustomer() {
    	Customer customer = customerService.createCustomer(
    			"John",
    			"Doe",
    			new Date());
    	
    	Customer actual = customerService.getCustomerById(customer.getId());
        
        assertThat(actual, is(not(nullValue())));
        assertThat(actual.getFirstName(), is(customer.getFirstName()));
        assertThat(actual.getLastName(), is(customer.getLastName()));
    	
    	return customer;
    }
    
    protected Registration createRegistration(Customer customer) {
    	Registration registration = registrationService.createRegistration(
    			customer, 
    			"01234567890123456789012345678901",
    			"VISA",
    			"420000",
    			"0000");
    	
    	Registration actual = registrationService.getRegistrationById(registration.getId());
        
        assertThat(actual, is(not(nullValue())));
        assertThat(actual.getCode(), is(registration.getCode()));
        assertThat(actual.getBrand(), is(registration.getBrand()));
        assertThat(actual.getBin(), is(registration.getBin()));
        assertThat(actual.getLast4Digits(), is(registration.getLast4Digits()));
    	
    	return registration;
    }
}
