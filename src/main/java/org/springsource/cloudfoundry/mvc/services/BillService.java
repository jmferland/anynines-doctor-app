package org.springsource.cloudfoundry.mvc.services;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.util.List;

@Service
@SuppressWarnings("unchecked")
@Transactional
public class BillService {

    public static final String BILLS_REGION = "bills";

    @PersistenceContext
    private EntityManager em;

    public Bill createBill(Merchant merchant, BigDecimal amount, String currency) {
        Bill bill = new Bill();
        bill.setMerchant(merchant);
        bill.setAmount(amount);
        bill.setCurrency(currency);
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

    @CacheEvict(BILLS_REGION)
    public void deleteBill(Integer id) {
        Bill bill = getBillById(id);
        em.remove(bill);
    }

    @CacheEvict(value = BILLS_REGION, key = "#id")
    public void updateBill(Integer id, Merchant merchant, BigDecimal amount, String currency) {
        Bill bill = getBillById(id);
        bill.setMerchant(merchant);
        bill.setAmount(amount);
        bill.setCurrency(currency);
        em.merge(bill);
    }
    
    @CacheEvict(value = BILLS_REGION, key = "#id")
    public void updateBillPayment(Integer id, Registration payment) {
        Bill bill = getBillById(id);
        bill.setPayment(payment);
        em.merge(bill);
    }
}
