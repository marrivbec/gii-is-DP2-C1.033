
package acme.features.flightCrewMember.flightAssigment;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.DutyType;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flightAssignment.Status;
import acme.entities.leg.Leg;
import acme.realms.employee.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssigmentShowService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssigmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean status;

		int flightAssignmentId;
		FlightAssignment flightAssignment;
		FlightCrewMember flightCrewMember;

		flightAssignmentId = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);
		flightCrewMember = flightAssignment == null ? null : flightAssignment.getFlightCrewMember();
		status = super.getRequest().getPrincipal().hasRealm(flightCrewMember);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		int id = super.getRequest().getData("id", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(id);

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {

		Leg leg = flightAssignment.getLeg();
		Date moment = MomentHelper.getCurrentMoment();

		Collection<Leg> l = this.repository.findAllLegsFromAirline(flightAssignment.getFlightCrewMember().getAirline().getId(), moment);

		if (!l.contains(flightAssignment.getLeg()) && flightAssignment.getLeg() != null)
			l.add(flightAssignment.getLeg());

		Dataset dataset = super.unbindObject(flightAssignment, "duty", "moment", "currentStatus", "remarks", "flightCrewMember", "leg", "draftMode");

		boolean isPastLeg = MomentHelper.getCurrentMoment().after(flightAssignment.getLeg().getScheduledArrival());
		dataset.put("pastLeg", isPastLeg);

		dataset.put("flightCrewMember", flightAssignment.getFlightCrewMember().getIdentity().getFullName());

		SelectChoices dutyChoices = SelectChoices.from(DutyType.class, flightAssignment.getDuty());
		dataset.put("dutyChoices", dutyChoices);

		SelectChoices statusChoices = SelectChoices.from(Status.class, flightAssignment.getCurrentStatus());
		dataset.put("statusChoices", statusChoices);

		SelectChoices legChoices = SelectChoices.from(l, "flightNumber", flightAssignment.getLeg());
		dataset.put("legChoices", legChoices);

		dataset.put("scheduledDeparture", leg.getScheduledDeparture());
		dataset.put("scheduledArrival", leg.getScheduledArrival());
		dataset.put("status", leg.getStatus());
		dataset.put("departureAirport", leg.getDepartureAirport().getName());
		dataset.put("arrivalAirport", leg.getArrivalAirport().getName());
		dataset.put("aircraft", leg.getAircraft().getRegistrationNumber());
		dataset.put("flight", leg.getFlight().getTag());

		super.getResponse().addData(dataset);
	}

}
