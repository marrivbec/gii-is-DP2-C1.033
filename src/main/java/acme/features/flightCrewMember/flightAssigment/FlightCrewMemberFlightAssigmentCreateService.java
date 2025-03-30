
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
public class FlightCrewMemberFlightAssigmentCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssigmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;
		Date moment;
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		moment = MomentHelper.getCurrentMoment();

		flightAssignment = new FlightAssignment();
		flightAssignment.setDuty(DutyType.LEAD_ATTENDANT);
		flightAssignment.setMoment(moment);
		flightAssignment.setCurrentStatus(Status.CONFIRMED);
		flightAssignment.setRemarks("");
		flightAssignment.setFlightCrewMember(flightCrewMember);

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {
		super.bindObject(flightAssignment, "duty", "moment", "currentStatus", "remarks", "leg");
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		;
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		Date moment;

		moment = MomentHelper.getCurrentMoment();
		flightAssignment.setMoment(moment);
		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Collection<Leg> legs;
		SelectChoices legChoice;
		SelectChoices dutyChoice;
		Dataset dataset;

		dutyChoice = SelectChoices.from(DutyType.class, flightAssignment.getDuty());
		legs = this.repository.findAllLeg();
		legChoice = SelectChoices.from(legs, "flightNumber", flightAssignment.getLeg());

		dataset = super.unbindObject(flightAssignment, "duty", "moment", "currentStatus", "remarks", "leg");
		dataset.put("readonly", false);
		dataset.put("duty", dutyChoice);
		dataset.put("legs", legChoice);

		super.getResponse().addData(dataset);

	}

}
