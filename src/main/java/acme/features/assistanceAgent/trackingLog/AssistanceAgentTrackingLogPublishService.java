/*
 * AssistanceAgentTrackingLogCreateService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.trackingLog.Indicator;
import acme.entities.trackingLog.TrackingLog;
import acme.realms.employee.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogPublishService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int trackingLogId;
		TrackingLog trackingLog;
		AssistanceAgent assistanceAgent;

		trackingLogId = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(trackingLogId);
		assistanceAgent = trackingLog == null ? null : trackingLog.getClaim().getAssistanceAgents();
		status = super.getRequest().getPrincipal().hasRealm(assistanceAgent) && (trackingLog == null || trackingLog.isDraftMode());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog trackingLog;
		int id;

		id = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(id);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackingLog) {
		super.bindObject(trackingLog, "lastUpdateMoment", "step", "resolutionPercentage", "indicator", "resolution", "claim");
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		;
	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		trackingLog.setDraftMode(false);
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Collection<Claim> claims;
		SelectChoices choices;
		SelectChoices choices2;
		AssistanceAgent assistanceAgent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
		Dataset dataset;

		choices = SelectChoices.from(Indicator.class, trackingLog.getIndicator());
		claims = this.repository.findAllClaim(assistanceAgent.getId());
		choices2 = SelectChoices.from(claims, "id", trackingLog.getClaim());

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "step", "resolutionPercentage", "indicator", "resolution", "claim", "draftMode");
		dataset.put("indicators", choices);
		dataset.put("claims", choices2);

		super.getResponse().addData(dataset);
	}

}
