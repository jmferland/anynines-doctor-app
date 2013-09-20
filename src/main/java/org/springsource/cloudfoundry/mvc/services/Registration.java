package org.springsource.cloudfoundry.mvc.services;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Registration implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
	private Date creationDate = new Date();
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;
	
	@NotNull
	@Size(min = 32, max = 32)
	@Column(unique = true)
	private String code;
	
	@NotNull
	private String brand;
	
	@NotNull
	private String bin;
	
	@Size(min = 4, max = 4)
	private String last4Digits;
	
	@Valid
	@Embedded
	private Expiry expiry = new Expiry();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getBin() {
		return bin;
	}

	public void setBin(String bin) {
		this.bin = bin;
	}

	public String getLast4Digits() {
		return last4Digits;
	}

	public void setLast4Digits(String last4Digits) {
		this.last4Digits = last4Digits;
	}

	public Expiry getExpiry() {
		return expiry;
	}

	public void setExpiry(Expiry expiry) {
		this.expiry = expiry;
	}
}
