package org.springsource.cloudfoundry.mvc.services;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
public class Bill implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "merchant_id", nullable = false)
	private Merchant merchant;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

    @NotNull
    private Date creationDate = new Date();

    @NotNull
    @Size(min = 32, max = 32)
    @Column(unique = true)
    private String token = UUID.randomUUID().toString().replace("-", "");
    
    @NotNull
    @Min(0)
    private BigDecimal amount;
    
    @NotNull
    @Size(min = 3, max = 3)
    private String currency;
    
    private String descriptor;
    
	@ManyToOne
	@JoinColumn(name = "payment_registration_id", nullable = true)
    private Registration payment;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Randomly generated token for publicly referring to bills (e.g. used in QR code)
	 * 
	 * @return
	 */
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * The {@link Registration} that was used to successfully pay this {@link Bill}, or null
	 * if not successfully paid.
	 * 
	 * @return
	 */
	public Registration getPayment() {
		return payment;
	}

	public void setPayment(Registration payment) {
		this.payment = payment;
	}

	public String getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}
}
