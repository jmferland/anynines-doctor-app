package org.springsource.cloudfoundry.mvc.services;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Collection;
import java.util.List;

@Service
@SuppressWarnings("unchecked")
@Transactional
public class MerchantService {

	public static final String MERCHANTS_REGION = "merchants";

    @PersistenceContext
    private EntityManager em;

    public Merchant createMerchant(String name, String securitySender,
    		String userLogin, String userPassword, String channelId) {
        Merchant merchant = new Merchant();
        merchant.setName(name);
        merchant.setSecuritySender(securitySender);
        merchant.setUserLogin(userLogin);
        merchant.setUserPassword(userPassword);
        merchant.setChannelId(channelId);
        em.persist(merchant);
        return merchant;
    }

    @Transactional(readOnly = true)
    public List<Merchant> getAllMerchants() {
        return em.createQuery("SELECT * FROM " + Merchant.class.getName()).getResultList();
    }
    
    @Cacheable(MERCHANTS_REGION)
    @Transactional(readOnly = true)
    public Merchant getMerchantById(Integer id) {
        return em.find(Merchant.class, id);
    }

    public Collection<Merchant> getMerchantByName(String name) {
        String sql = "select m.* from merchant m where m.name = :nm";
        return em.createNativeQuery(sql, Merchant.class)
                .setParameter("nm", name)
                .getResultList();
    }

    @CacheEvict(MERCHANTS_REGION)
    public void deleteMerchant(Integer id) {
    	Merchant merchant = getMerchantById(id);
        em.remove(merchant);
    }

    @CacheEvict(value = MERCHANTS_REGION, key = "#id")
    public void updateMerchant(Integer id, String name, String securitySender,
    		String userLogin, String userPassword, String channelId) {
    	Merchant merchant = getMerchantById(id);
        merchant.setName(name);
        merchant.setSecuritySender(securitySender);
        merchant.setUserLogin(userLogin);
        merchant.setUserPassword(userPassword);
        merchant.setChannelId(channelId);
        em.merge(merchant);
    }
}
