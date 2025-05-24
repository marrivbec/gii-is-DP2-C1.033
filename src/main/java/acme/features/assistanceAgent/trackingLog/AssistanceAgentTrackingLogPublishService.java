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
import java.util.List;
import java.util.stream.Collectors;

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
		super.bindObject(trackingLog, "step", "resolutionPercentage", "indicator", "resolution");
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		List<TrackingLog> previousLogs = this.repository.findTrackingLogsByClaimIdOrderedByPercentage(trackingLog.getClaim().getId());
		Collection<TrackingLog> logsWith100 = this.repository.findLogsWith100(trackingLog.getClaim().getId());
		Claim claim = trackingLog.getClaim();

		if (!previousLogs.isEmpty()) {
			TrackingLog lastLog = previousLogs.get(0);
			if (lastLog.getResolutionPercentage() != null && trackingLog.getResolutionPercentage() != null && trackingLog.getId() != lastLog.getId())
				if (lastLog.getResolutionPercentage() == 100.00 && trackingLog.getResolutionPercentage() == 100.00)
					// solo puede repetirse el 100% si está publicada
					if (lastLog.isDraftMode())
						super.state(false, "resolutionPercentage", "acme.validation.trackingLog.publish.message");

		}

		if (logsWith100.stream().filter(x -> !x.isDraftMode()).collect(Collectors.toList()).size() + 1 > 2 && trackingLog.getResolutionPercentage() == 100)
			super.state(false, "resolutionPercentage", "acme.validation.trackingLog.publish.message.completed");

		if (claim != null && claim.isDraftMode())
			super.state(false, "*", "acme.validation.trackingLog.unpublished.message");
	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		trackingLog.setDraftMode(false);
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(Indicator.class, trackingLog.getIndicator());

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "step", "resolutionPercentage", "indicator", "resolution", "draftMode");
		dataset.put("masterId", trackingLog.getClaim().getId());
		dataset.put("indicators", choices);

		super.getResponse().addData(dataset);
	}

}
