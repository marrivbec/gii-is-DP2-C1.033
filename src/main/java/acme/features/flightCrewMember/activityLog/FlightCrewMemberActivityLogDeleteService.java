
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.realms.employee.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogDeleteService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

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
		flightCrewMember = activityLog == null ? null : activityLog.getFlightAssignment().getFlightCrewMember();
		status = super.getRequest().getPrincipal().hasRealm(flightCrewMember) && (activityLog == null || activityLog.isDraftMode());
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog activityLog;
		int id;

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
		;
	}

	@Override
	public void perform(final ActivityLog activityLog) {

		this.repository.delete(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {

		//		Dataset dataset;
		//		int flightCrewMemberId;
		//		Date currentMoment;
		//
		//		currentMoment = MomentHelper.getCurrentMoment();
		//		flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		//		List<FlightAssignment> assignments;
		//		assignments = this.repository.findAssignmentsByMemberIdCompletedLegs(currentMoment, flightCrewMemberId);
		//
		//		SelectChoices assignmentChoices;
		//
		//		assignmentChoices = SelectChoices.from(assignments, "leg.flightNumber", activityLog.getFlightAssignment());
		//
		//		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel");
		//		dataset.put("assignment", assignmentChoices.getSelected().getKey());
		//		dataset.put("assignmentChoices", assignmentChoices);
		//		//		dataset.put("faId", activityLog.getFlightAssignment().getId());
		//		//		dataset.put("fadraftMode", activityLog.getFlightAssignment().isDraftMode());
		//
		//		super.getResponse().addData(dataset);
		Dataset dataset;
		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");
		dataset.put("masterId", activityLog.getFlightAssignment().getId());

		super.getResponse().addData(dataset);

	}

}
