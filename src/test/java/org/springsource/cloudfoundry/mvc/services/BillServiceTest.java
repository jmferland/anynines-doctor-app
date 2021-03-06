package org.springsource.cloudfoundry.mvc.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;
import java.util.Collection;

import org.junit.Test;

public class BillServiceTest extends BaseServiceTest {
    
    @Test
    public void testCreateBill_Success() {
        createBill(createMerchant("1"), createCustomer());
    }

    @Test
    public void testUpdateBill_Success() {
        Bill bill = createBill(createMerchant("1"), createCustomer());
        
        Merchant merchantTwo = createMerchant("2");
        Customer customerTwo = createCustomer();
        BigDecimal amountTwo = new BigDecimal("2.00");
        String currencyTwo = "USD";
        String descriptorTwo = "descriptor2";
        billService.updateBill(bill.getId(), merchantTwo, customerTwo, amountTwo, currencyTwo, descriptorTwo);
        bill = billService.getBillById(bill.getId());
        
        assertThat(bill, is(not(nullValue())));
        assertThat(bill.getMerchant().getId(), is(merchantTwo.getId()));
        assertThat(bill.getCustomer().getId(), is(customerTwo.getId()));
        assertThat(bill.getAmount(), is(amountTwo));
        assertThat(bill.getCurrency(), is(currencyTwo));
        assertThat(bill.getDescriptor(), is(descriptorTwo));
    }
    
    @Test
    public void testSearch_Success() {
        Bill bill = createBill(createMerchant("1"), createCustomer());
        
        assertSearchResult(bill.getMerchant().getName(), bill);
        assertSearchResult(bill.getCustomer().getFirstName(), bill);
        assertSearchResult(bill.getDescriptor(), bill);
    }
    
    private void assertSearchResult(String query, Bill bill) {
    	Collection<Bill> results = billService.search(query);
        assertThat(results.size(), is(1));
        assertThat(results.iterator().next().getId(), is(bill.getId()));
    }

    @Test
    public void testUpdateBillPayment_Success() {
        Bill bill = createBill(createMerchant("1"), createCustomer());
        
        Customer customer = createCustomer();
        Registration registration = createRegistration(customer);
        billService.updateBillPayment(bill.getId(), registration);
        
        bill = billService.getBillById(bill.getId());
        
        assertThat(bill, is(not(nullValue())));
        assertThat(bill.getPayment(), is(not(nullValue())));
    }
    
    @Test
    public void testGetBillByToken_Success() {    	
        Bill bill = createBill(createMerchant("1"), createCustomer());
        bill = billService.getBillByToken(bill.getToken());
        
        assertThat(bill, is(not(nullValue())));
        assertThat(bill.getToken(), is(bill.getToken()));
    }
}
