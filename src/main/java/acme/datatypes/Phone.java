/*
 * Phone.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.datatypes;

import javax.persistence.Embeddable;

import acme.client.components.basis.AbstractDatatype;
import acme.constraints.ValidPhone;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@ValidPhone
public class Phone extends AbstractDatatype {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private Integer				countryCode;

	private Integer				areaCode;

	private String				number;

	// Object interface -------------------------------------------------------


	@Override
	public String toString() {
		StringBuilder result;

		result = new StringBuilder();
		result.append("<<+");
		result.append(this.countryCode);
		if (this.areaCode == null)
			result.append(" ");
		else {
			result.append(" (");
			result.append(this.areaCode);
			result.append(") ");
		}
		result.append(this.number);
		result.append(">>");

		return result.toString();
	}

}
