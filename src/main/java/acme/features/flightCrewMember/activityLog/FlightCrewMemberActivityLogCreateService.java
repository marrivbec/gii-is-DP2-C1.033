
package acme.features.flightCrewMember.activityLog;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.employee.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogCreateService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean status;
		int masterId;
		FlightAssignment flightAssignment;

		masterId = super.getRequest().getData("masterId", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(masterId);

		int id = 0;

		if (super.getRequest().getMethod().equals("POST"))
			id = super.getRequest().getData("id", int.class, 0);

		status = flightAssignment != null && super.getRequest().getPrincipal().hasRealm(flightAssignment.getFlightCrewMember()) && id == 0;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		ActivityLog activityLog;
		Date registrationMoment;
		int masterId;
		FlightAssignment flightAssignment;

		masterId = super.getRequest().getData("masterId", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(masterId);
		registrationMoment = MomentHelper.getCurrentMoment();

		activityLog = new ActivityLog();
		activityLog.setRegistrationMoment(registrationMoment);
		activityLog.setTypeOfIncident("");
		activityLog.setDescription("");
		activityLog.setSeverityLevel(0);
		activityLog.setDraftMode(true);
		activityLog.setFlightAssignment(flightAssignment);

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

		Date registrationMoment;

		registrationMoment = MomentHelper.getCurrentMoment();
		activityLog.setRegistrationMoment(registrationMoment);

		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {

		Dataset dataset;
		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");
		dataset.put("readonly", false);
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));

		super.getResponse().addData(dataset);

	}

}
