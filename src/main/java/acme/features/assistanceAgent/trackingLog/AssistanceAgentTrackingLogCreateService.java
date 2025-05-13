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
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.trackingLog.Indicator;
import acme.entities.trackingLog.TrackingLog;
import acme.realms.employee.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogCreateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Claim claim;

		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimById(masterId);
		status = claim != null && super.getRequest().getPrincipal().hasRealm(claim.getAssistanceAgents());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog trackingLog;
		Date lastUpdateMoment;
		int masterId;
		Claim claim;

		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimById(masterId);
		lastUpdateMoment = MomentHelper.getCurrentMoment();

		trackingLog = new TrackingLog();
		trackingLog.setLastUpdateMoment(lastUpdateMoment);
		trackingLog.setStep("");
		trackingLog.setResolutionPercentage(0.0);
		trackingLog.setIndicator(Indicator.PENDING);
		trackingLog.setResolution("");
		trackingLog.setDraftMode(true);
		trackingLog.setClaim(claim);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackingLog) {
		super.bindObject(trackingLog, "step", "resolutionPercentage", "indicator", "resolution");
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		int claimId;
		claimId = super.getRequest().getData("masterId", int.class);
		List<TrackingLog> previousLogs = this.repository.findTrackingLogsByClaimIdOrderedByPercentage(claimId);
		Collection<TrackingLog> logsWith100 = this.repository.findLogsWith100(claimId);

		if (!previousLogs.isEmpty()) {
			TrackingLog lastLog = previousLogs.get(0);
			if (lastLog.getResolutionPercentage() != null && trackingLog.getResolutionPercentage() != null)
				if (lastLog.getResolutionPercentage() == 100.00 && trackingLog.getResolutionPercentage() == 100.00)
					// solo puede repetirse el 100% si está publicada
					if (lastLog.isDraftMode())
						super.state(false, "resolutionPercentage", "acme.validation.trackingLog.publish.message");

		}
		if (logsWith100.size() + 1 > 2)
			super.state(false, "resolutionPercentage", "acme.validation.trackingLog.publish.message.completed");

	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		Date moment;

		moment = MomentHelper.getCurrentMoment();
		trackingLog.setLastUpdateMoment(moment);
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(Indicator.class, trackingLog.getIndicator());

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "step", "resolutionPercentage", "indicator", "resolution", "draftMode");
		dataset.put("readonly", false);
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));
		dataset.put("indicators", choices);

		super.getResponse().addData(dataset);
	}

}
