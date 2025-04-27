/*
 * AssistanceAgentDashboard.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import acme.datatypes.Statistics;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssistanceAgentDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	Double						resolvedClaimsRatio;
	Double						rejectedClaimsRatio;
	List<String>				topThreeMonthsWithHighestClaims;
	Statistics					claimLogsStatistics;
	Statistics					claimsAssistedLastMonthStatistics;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
