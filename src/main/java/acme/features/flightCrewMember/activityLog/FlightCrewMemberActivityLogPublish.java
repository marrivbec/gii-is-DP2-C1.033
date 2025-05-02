
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.employee.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogPublish extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int activityLogId;
		ActivityLog activityLog;
		FlightCrewMember flightCrewMember;

		activityLogId = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(activityLogId);
		flightCrewMember = activityLog == null ? null : activityLog.getFlightAssignment().getFlightCrewMember();
		status = super.getRequest().getPrincipal().hasRealm(flightCrewMember) && (activityLog == null || activityLog.isDraftMode());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		ActivityLog activityLog;

		id = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(id);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {

		super.bindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel");

	}

	@Override
	public void validate(final ActivityLog activityLog) {
		boolean status;

		FlightAssignment flightAssignment = activityLog.getFlightAssignment();
		status = flightAssignment != null && !flightAssignment.isDraftMode();

		super.state(status, "*", "acme.validation.activity.unpublished.message");
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		activityLog.setDraftMode(false);
		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;
		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");
		dataset.put("masterId", activityLog.getFlightAssignment().getId());

		super.getResponse().addData(dataset);

	}

}
