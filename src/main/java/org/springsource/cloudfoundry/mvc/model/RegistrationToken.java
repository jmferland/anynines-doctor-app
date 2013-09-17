package org.springsource.cloudfoundry.mvc.model;

import org.springsource.cloudfoundry.mvc.services.Registration;

/**
 * Registration associated with a (COPYandPAY) token already generated.
 * 
 * @author jferland
 */
public class RegistrationToken implements Comparable<RegistrationToken> {
	private final Registration registration;
	private final String token;

	public RegistrationToken(Registration registration, String token) {
		this.registration = registration;
		this.token = token;
	}

	public Registration getRegistration() {
		return registration;
	}

	public String getToken() {
		return token;
	}

	@Override
	public int compareTo(RegistrationToken other) {
		return registration.getCreationDate().compareTo(other.getRegistration().getCreationDate());
	}
}
