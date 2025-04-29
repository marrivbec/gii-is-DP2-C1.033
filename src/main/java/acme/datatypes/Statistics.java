/*
 * Statistics.java
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

import acme.client.components.basis.AbstractDatatype;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Statistics extends AbstractDatatype {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private Double				average;

	private Double				minimum;

	private Double				maximum;

	private Double				deviation;

	private Integer				count;

	// Object interface -------------------------------------------------------


	@Override
	public String toString() {
		return String.format("Statistics [average=%.2f, minimum=%.2f, maximum=%.2f, deviation=%.2f, count=%s]", this.average, this.minimum, this.maximum, this.deviation, this.count);
	}

}
