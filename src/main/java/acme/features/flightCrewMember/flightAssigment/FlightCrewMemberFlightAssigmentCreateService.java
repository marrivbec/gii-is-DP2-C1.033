
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
import acme.realms.employee.AvailabilityStatus;
import acme.realms.employee.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssigmentCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssigmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean status;
		String method;
		Leg leg;
		int legId;

		Date moment = MomentHelper.getCurrentMoment();

		method = super.getRequest().getMethod();

		if (method.equals("GET"))
			status = true;
		else {
			legId = super.getRequest().getData("leg", int.class);
			leg = this.repository.findLegById(legId);
			FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

			Collection<Leg> l = this.repository.findAllLegsFromAirline(flightCrewMember.getAirline().getId(), moment);

			int id = super.getRequest().getData("id", int.class, 0);
			status = legId == 0 || leg != null && leg.getAircraft().getAirline().equals(flightCrewMember.getAirline()) && id == 0 && l.contains(leg);

		}

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {

		FlightAssignment flightAssignment = new FlightAssignment();

		flightAssignment.setMoment(MomentHelper.getCurrentMoment());

		flightAssignment.setDraftMode(true);

		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		flightAssignment.setFlightCrewMember(flightCrewMember);

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {

		super.bindObject(flightAssignment, "duty", "currentStatus", "remarks", "leg");
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {

		if (flightAssignment.getDuty() != null && flightAssignment.getLeg() != null) {
			boolean isDutyAssigned = this.repository.hasDutyAssigned(flightAssignment.getLeg().getId(), flightAssignment.getDuty(), flightAssignment.getId());
			super.state(!isDutyAssigned, "duty", "acme.validation.flightAssignment.duty");
		}

		if (flightAssignment.getFlightCrewMember() != null && flightAssignment.getLeg() != null) {
			boolean isAvailable = flightAssignment.getFlightCrewMember().getAvailabilityStatus().equals(AvailabilityStatus.AVAILABLE);
			super.state(isAvailable, "flightCrewMember", "acme.validation.flightAssignment.flightCrewMember.available");
			boolean isAssigned = this.repository.hasFlightCrewMemberLegAssociated(flightAssignment.getFlightCrewMember().getId(), flightAssignment.getLeg().getScheduledArrival(), flightAssignment.getLeg().getScheduledDeparture(), flightAssignment.getId());
			super.state(!isAssigned, "flightCrewMember", "acme.validation.flightAssignment.flightCrewMember.multipleLegs");
		}

	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {

		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {

		Date moment = MomentHelper.getCurrentMoment();

		Dataset dataset = super.unbindObject(flightAssignment, "duty", "moment", "currentStatus", "draftMode", "remarks", "flightCrewMember", "leg");

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
