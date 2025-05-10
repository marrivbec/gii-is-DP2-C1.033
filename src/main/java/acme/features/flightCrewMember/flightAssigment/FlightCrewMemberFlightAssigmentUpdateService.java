
package acme.features.flightCrewMember.flightAssigment;

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
public class FlightCrewMemberFlightAssigmentUpdateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	//Internal state---------------------------------------------------------

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

		id = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(id);
		flightCrewMember = flightAssignment == null ? null : flightAssignment.getFlightCrewMember();
		status = super.getRequest().getPrincipal().hasRealm(flightCrewMember) && (flightAssignment == null || flightAssignment.isDraftMode());

		method = super.getRequest().getMethod();

		if (method.equals("GET"))
			status2 = status;
		else {
			legId = super.getRequest().getData("leg", int.class);
			leg = this.repository.findLegById(legId);
			status2 = legId == 0 || leg != null;
		}

		super.getResponse().setAuthorised(status2);
	}

	@Override
	public void load() {
		int flightAssignmentId = super.getRequest().getData("id", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);

		flightAssignment.setMoment(MomentHelper.getCurrentMoment());

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {
		super.bindObject(flightAssignment, "duty", "currentStatus", "remarks", "leg");
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		if (flightAssignment.getLeg() != null) {
			boolean isPastLeg = flightAssignment.getLeg().getScheduledDeparture().before(MomentHelper.getCurrentMoment());
			super.state(!isPastLeg, "leg", "acme.validation.flightAssignment.leg.moment");
		}

	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset = super.unbindObject(flightAssignment, "duty", "moment", "currentStatus", "remarks", "draftMode", "flightCrewMember", "leg");

		dataset.put("flightCrewMember", flightAssignment.getFlightCrewMember().getIdentity().getFullName());

		SelectChoices dutyChoices = SelectChoices.from(DutyType.class, flightAssignment.getDuty());
		dataset.put("dutyChoices", dutyChoices);
		dataset.put("duty", dutyChoices.getSelected().getKey());

		SelectChoices statusChoices = SelectChoices.from(Status.class, flightAssignment.getCurrentStatus());
		dataset.put("statusChoices", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());

		SelectChoices legChoices = SelectChoices.from(this.repository.findAllLegsFromAirline(flightAssignment.getFlightCrewMember().getAirline().getId()), "flightNumber", flightAssignment.getLeg());
		dataset.put("legChoices", legChoices);
		dataset.put("leg", legChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
