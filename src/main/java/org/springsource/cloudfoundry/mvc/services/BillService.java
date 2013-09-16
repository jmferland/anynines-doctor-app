package org.springsource.cloudfoundry.mvc.services;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Service
@SuppressWarnings("unchecked")
@Transactional
public class BillService {

    public static final String BILLS_REGION = "bills";

    @PersistenceContext
    private EntityManager em;

    public Bill createBill(Merchant merchant, Customer customer, BigDecimal amount, String currency, String descriptor) {
        Bill bill = new Bill();
        bill.setMerchant(merchant);
        bill.setCustomer(customer);
        bill.setAmount(amount);
        bill.setCurrency(currency);
        bill.setDescriptor(descriptor);
        em.persist(bill);
        return bill;
    }

    @Transactional(readOnly = true)
    public List<Bill> getAllBills() {
        return em.createQuery("from " + Bill.class.getName()).getResultList();
    }
    
    @Cacheable(BILLS_REGION)
    @Transactional(readOnly = true)
    public Bill getBillById(Integer id) {
        return em.find(Bill.class, id);
    }

    public Bill getBillByToken(String token) {
        String sql = "select b.* from bill b where b.token = :tk";
        return (Bill) em.createNativeQuery(sql, Bill.class)
                .setParameter("tk", token)
                .getSingleResult();
    }
    
    public Collection<Bill> search(String query) {
        String lcQuery = ("%" + query + "%").toLowerCase();
        String sql = "select b.* from bill b, merchant m, customer c where" +
        		" b.merchant_id = m.id" +
        		" AND b.customer_id = c.id" +
        		" AND (" +
        		"    LOWER( b.token ) LIKE :q" +
        		" OR LOWER( b.currency ) LIKE :q" +
        		" OR LOWER( b.descriptor ) LIKE :q" +
        		" OR LOWER( m.name ) LIKE :q" +
        		" OR LOWER( m.securitySender ) LIKE :q" +
        		" OR LOWER( m.userLogin ) LIKE :q" +
        		" OR LOWER( m.userPassword ) LIKE :q" +
        		" OR LOWER( m.channelId ) LIKE :q" +
        		" OR LOWER( c.firstName ) LIKE :q" +
        		" OR LOWER( c.lastName ) LIKE :q" +
        		" )";
        return em.createNativeQuery(sql, Bill.class)
                .setParameter("q", lcQuery)
                .getResultList();
    }

    @CacheEvict(BILLS_REGION)
    public void deleteBill(Integer id) {
        Bill bill = getBillById(id);
        em.remove(bill);
    }

    @CacheEvict(value = BILLS_REGION, key = "#id")
    public void updateBill(Integer id, Merchant merchant, Customer customer, BigDecimal amount, String currency, String descriptor) {
        Bill bill = getBillById(id);
        bill.setMerchant(merchant);
        bill.setCustomer(customer);
        bill.setAmount(amount);
        bill.setCurrency(currency);
        bill.setDescriptor(descriptor);
        em.merge(bill);
    }
    
    @CacheEvict(value = BILLS_REGION, key = "#id")
    public void updateBillPayment(Integer id, Registration payment) {
        Bill bill = getBillById(id);
        bill.setPayment(payment);
        em.merge(bill);
    }
}
