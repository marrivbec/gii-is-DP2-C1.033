
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
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
		ActivityLog activityLog;
		int id;
		FlightCrewMember flightCrewMember;

		id = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(id);
		FlightCrewMember fcm = activityLog == null ? null : activityLog.getFlightAssignment().getFlightCrewMember();
		flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		status = flightCrewMember.equals(fcm) && activityLog != null && activityLog.isDraftMode() && !activityLog.getFlightAssignment().isDraftMode();

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

		super.bindObject(activityLog, "typeOfIncident", "description", "severityLevel");

	}

	@Override
	public void validate(final ActivityLog activityLog) {

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
		dataset.put("fadf", activityLog.getFlightAssignment().isDraftMode());
		dataset.put("masterId", activityLog.getFlightAssignment().getId());

		super.getResponse().addData(dataset);

	}

}
