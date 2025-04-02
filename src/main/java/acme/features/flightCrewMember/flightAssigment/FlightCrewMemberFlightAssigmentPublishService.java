
package acme.features.flightCrewMember.flightAssigment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.DutyType;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flightAssignment.Status;
import acme.realms.employee.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssigmentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

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
		int flightAssignmentId = super.getRequest().getData("id", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {
		super.bindObject(flightAssignment, "duty", "currentStatus", "remarks", "leg");
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		super.state(flightAssignment.getFlightCrewMember() != null, "flightCrewMember", "acme.validation.flightAssignment.flightcrewmember");
		super.state(flightAssignment.getLeg() != null, "leg", "acme.validation.flightAssignment.leg");

		if (flightAssignment.getDuty() != null && flightAssignment.getLeg() != null) {
			boolean isDutyAssigned = this.repository.hasDutyAssigned(flightAssignment.getLeg().getId(), flightAssignment.getDuty(), flightAssignment.getId());
			super.state(!isDutyAssigned, "duty", "acme.validation.flightAssignment.duty");
		}

		if (flightAssignment.getLeg() != null) {
			boolean isPastLeg = flightAssignment.getLeg().getScheduledDeparture().before(MomentHelper.getCurrentMoment());
			super.state(!isPastLeg, "leg", "acme.validation.flightAssignment.leg.moment");
		}

	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		flightAssignment.setDraftMode(false);
		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset = super.unbindObject(flightAssignment, "duty", "moment", "currentStatus", "remarks", "draftMode", "flightCrewMember", "leg");

		SelectChoices dutyChoices = SelectChoices.from(DutyType.class, flightAssignment.getDuty());
		dataset.put("dutyChoices", dutyChoices);

		SelectChoices statusChoices = SelectChoices.from(Status.class, flightAssignment.getCurrentStatus());
		dataset.put("statusChoices", statusChoices);

		Collection<FlightCrewMember> flightCrewMembers = this.repository.findAllflightCrewMemberFromAirline(flightAssignment.getFlightCrewMember().getAirline().getId());
		SelectChoices flightCrewMemberChoices = SelectChoices.from(flightCrewMembers, "identity.fullName", flightAssignment.getFlightCrewMember());
		dataset.put("flightCrewMemberChoices", flightCrewMemberChoices);
		dataset.put("readOnlyCrewMember", true);

		SelectChoices legChoices = SelectChoices.from(this.repository.findAllLegs(), "flightNumber", flightAssignment.getLeg());
		dataset.put("legChoices", legChoices);

		super.getResponse().addData(dataset);
	}

}
