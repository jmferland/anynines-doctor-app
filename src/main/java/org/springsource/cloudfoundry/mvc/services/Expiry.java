package org.springsource.cloudfoundry.mvc.services;

import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

@Embeddable
public class Expiry {
	@Size(min = 4, max = 4)
	private String year;

	@Size(min = 2, max = 2)
	private String month;
	
	public String getMonth() {
		return month;
	}

	public Expiry setMonth(String month) {
		this.month = month;
		return this;
	}

	public String getYear() {
		return year;
	}

	public Expiry setYear(String year) {
		this.year = year;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((month == null) ? 0 : month.hashCode());
		result = prime * result + ((year == null) ? 0 : year.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Expiry other = (Expiry) obj;
		if (month == null) {
			if (other.month != null)
				return false;
		} else if (!month.equals(other.month))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}
}
