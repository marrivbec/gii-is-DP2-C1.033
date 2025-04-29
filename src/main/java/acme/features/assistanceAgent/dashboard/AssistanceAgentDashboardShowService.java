/*
 * AssistanceAgentDashboardShowService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.assistanceAgent.dashboard;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.Statistics;
import acme.forms.AssistanceAgentDashboard;
import acme.realms.employee.AssistanceAgent;

@GuiService
public class AssistanceAgentDashboardShowService extends AbstractGuiService<AssistanceAgent, AssistanceAgentDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		AssistanceAgent agent;

		agent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
		Principal principal = super.getRequest().getPrincipal();
		status = principal.hasRealm(agent);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AssistanceAgentDashboard dashboard;
		Double resolvedClaimsRatio;
		Double rejectedClaimsRatio;
		List<Object[]> topThreeMonthsWithHighestClaims;
		AssistanceAgent agent;

		agent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();

		resolvedClaimsRatio = this.repository.resolvedClaimsRatio(agent);
		rejectedClaimsRatio = this.repository.rejectedClaimsRatio(agent);
		topThreeMonthsWithHighestClaims = this.repository.topThreeMonthsWithHighestClaims(agent);
		topThreeMonthsWithHighestClaims.stream().limit(3).collect(Collectors.toList());

		Statistics claimLogsStatistics = new Statistics();
		claimLogsStatistics.setAverage(this.repository.averageNumberOfLogsPerClaim(agent));
		claimLogsStatistics.setMinimum(this.repository.minimumNumberOfLogsPerClaim(agent));
		claimLogsStatistics.setMaximum(this.repository.maximumNumberOfLogsPerClaim(agent));
		claimLogsStatistics.setDeviation(this.repository.deviationNumberOfLogsPerClaim(agent));

		dashboard = new AssistanceAgentDashboard();
		dashboard.setResolvedClaimsRatio(resolvedClaimsRatio);
		dashboard.setRejectedClaimsRatio(rejectedClaimsRatio);
		dashboard.setTopThreeMonthsWithHighestClaims(topThreeMonthsWithHighestClaims);
		dashboard.setClaimLogsStatistics(claimLogsStatistics);

		//		dashboard.setClaimsAssistedLastMonthStatistics(claimsAssistedLastMonthStatistics);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final AssistanceAgentDashboard dashboard) {
		Dataset dataset;

		dataset = super.unbindObject(dashboard, //
			"resolvedClaimsRatio", "rejectedClaimsRatio", // 
			"topThreeMonthsWithHighestClaims", "claimLogsStatistics" //
		//			"claimsAssistedLastMonthStatistics"
		);

		super.getResponse().addData(dataset);
	}

}
