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
public class RegistrationService {

	public static final String REGISTRATIONS_REGION = "registrations";

    @PersistenceContext
    private EntityManager em;

    public Registration createRegistration(Customer customer, String code,
    		String brand, String bin, String last4Digits) {
        Registration registration = new Registration();
        registration.setCustomer(customer);
        registration.setCode(code);
        registration.setBrand(brand);
        registration.setBin(bin);
        registration.setLast4Digits(last4Digits);
        em.persist(registration);
        return registration;
    }

    @Transactional(readOnly = true)
    public List<Registration> getAllRegistrations() {
        return em.createQuery("from " + Registration.class.getName()).getResultList();
    }
    
    @Cacheable(REGISTRATIONS_REGION)
    @Transactional(readOnly = true)
    public Registration getRegistrationById(Integer id) {
        return em.find(Registration.class, id);
    }

    public Collection<Registration> getRegistrationByCode(String code) {
        String sql = "select r.* from registration r where r.code = :cd";
        return em.createNativeQuery(sql, Registration.class)
                .setParameter("cd", code)
                .getResultList();
    }
    
    public Collection<Registration> search(String query) {
        String lcQuery = ("%" + query + "%").toLowerCase();
        String sql = "select r.* from registration r, customer c where" +
        		" r.customer_id = c.id" +
        		" AND (" +
        		"    LOWER( r.code ) LIKE :q" +
        		" OR LOWER( r.brand ) LIKE :q" +
        		" OR LOWER( r.bin ) LIKE :q" +
        		" OR LOWER( r.last4Digits ) LIKE :q" +
        		" OR LOWER( c.firstName ) LIKE :q" +
        		" OR LOWER( c.lastName ) LIKE :q" +
        		" )";
        return em.createNativeQuery(sql, Registration.class)
                .setParameter("q", lcQuery)
                .getResultList();
    }

    @CacheEvict(REGISTRATIONS_REGION)
    public void deleteRegistration(Integer id) {
    	Registration registration = getRegistrationById(id);
        em.remove(registration);
    }

    @CacheEvict(value = REGISTRATIONS_REGION, key = "#id")
    public void updateRegistration(Integer id, Customer customer, String code,
    		String brand, String bin, String last4Digits) {
    	Registration registration = getRegistrationById(id);
        registration.setCustomer(customer);
        registration.setCode(code);
        registration.setBrand(brand);
        registration.setBin(bin);
        registration.setLast4Digits(last4Digits);
        em.merge(registration);
    }
}
