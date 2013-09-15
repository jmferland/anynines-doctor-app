package org.springsource.cloudfoundry.mvc.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;

import org.junit.Test;

public class BillServiceTest extends BaseServiceTest {
    
    @Test
    public void testCreateBill_Success() {
        createBill(createMerchant("1"));
    }

    @Test
    public void testUpdateBill_Success() {
        Bill bill = createBill(createMerchant("1"));
        
        Merchant merchantTwo = createMerchant("2");
        BigDecimal amountTwo = new BigDecimal("2.00");
        String currencyTwo = "USD";
        billService.updateBill(bill.getId(), merchantTwo, amountTwo, currencyTwo);
        bill = billService.getBillById(bill.getId());
        
        assertThat(bill, is(not(nullValue())));
        assertThat(bill.getMerchant().getName(), is(merchantTwo.getName()));
        assertThat(bill.getAmount(), is(amountTwo));
        assertThat(bill.getCurrency(), is(currencyTwo));
    }

    @Test
    public void testUpdateBillPayment_Success() {
        Bill bill = createBill(createMerchant("1"));
        
        Customer customer = createCustomer();
        Registration registration = createRegistration(customer);
        billService.updateBillPayment(bill.getId(), registration);
        
        bill = billService.getBillById(bill.getId());
        
        assertThat(bill, is(not(nullValue())));
        assertThat(bill.getPayment(), is(not(nullValue())));
    }
    
    @Test
    public void testGetBillByToken_Success() {    	
        Bill bill = createBill(createMerchant("1"));
        bill = billService.getBillByToken(bill.getToken());
        
        assertThat(bill, is(not(nullValue())));
        assertThat(bill.getToken(), is(bill.getToken()));
    }
}
