
package acme.features.flightCrewMember.flightAssigment;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.DutyType;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flightAssignment.Status;
import acme.entities.leg.Leg;
import acme.realms.employee.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssigmentDeleteService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssigmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean status;
		boolean status2;
		FlightAssignment flightAssignment;
		int id;
		FlightCrewMember flightCrewMember;
		String method;
		Leg leg;
		int legId;

		Date moment = MomentHelper.getCurrentMoment();

		id = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(id);
		FlightCrewMember fcm = flightAssignment == null ? null : flightAssignment.getFlightCrewMember();
		flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		status = flightCrewMember.equals(fcm) && flightAssignment != null && flightAssignment.isDraftMode();

		method = super.getRequest().getMethod();

		if (method.equals("GET"))
			status2 = true;
		else {
			legId = super.getRequest().getData("leg", int.class);
			leg = this.repository.findLegById(legId);

			Collection<Leg> l = this.repository.findAllLegsFromAirline(flightCrewMember.getAirline().getId(), moment);

			status2 = legId == 0 || leg != null && leg.getAircraft().getAirline().equals(flightCrewMember.getAirline()) && status && (l.contains(leg) || flightAssignment.getLeg().equals(leg));
		}

		super.getResponse().setAuthorised(status && status2);
	}

	@Override
	public void load() {

		int id = super.getRequest().getData("id", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(id);

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {

		super.bindObject(flightAssignment, "duty", "currentStatus", "remarks", "leg");
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {

		;

	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		Collection<ActivityLog> activityLogs;

		activityLogs = this.repository.findActivityLogsByFlightAssignmentId(flightAssignment.getId());
		this.repository.deleteAll(activityLogs);
		this.repository.delete(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {

		Date moment = MomentHelper.getCurrentMoment();

		Dataset dataset = super.unbindObject(flightAssignment, "duty", "moment", "currentStatus", "draftMode", "remarks", "flightCrewMember", "leg");

		boolean isPastLeg = MomentHelper.getCurrentMoment().after(flightAssignment.getLeg().getScheduledArrival());
		dataset.put("pastLeg", isPastLeg);

		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		dataset.put("flightCrewMember", flightCrewMember.getIdentity().getFullName());

		SelectChoices dutyChoices = SelectChoices.from(DutyType.class, flightAssignment.getDuty());
		dataset.put("dutyChoices", dutyChoices);
		dataset.put("duty", dutyChoices.getSelected().getKey());

		SelectChoices statusChoices = SelectChoices.from(Status.class, flightAssignment.getCurrentStatus());
		dataset.put("statusChoices", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());

		SelectChoices legChoices = SelectChoices.from(this.repository.findAllLegsFromAirline(flightCrewMember.getAirline().getId(), moment), "flightNumber", flightAssignment.getLeg());
		dataset.put("legChoices", legChoices);
		dataset.put("leg", legChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
