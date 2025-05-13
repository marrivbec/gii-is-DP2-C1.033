
package acme.features.flightCrewMember.flightAssigment;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.DutyType;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flightAssignment.Status;
import acme.realms.employee.AvailabilityStatus;
import acme.realms.employee.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssigmentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

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
		status = super.getRequest().getPrincipal().hasRealm(flightCrewMember) && (flightAssignment == null || flightAssignment.isDraftMode());

		super.getResponse().setAuthorised(status);

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
		flightAssignment.setDraftMode(false);
		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset = super.unbindObject(flightAssignment, "duty", "moment", "currentStatus", "remarks", "draftMode", "flightCrewMember", "leg");

		dataset.put("flightCrewMember", flightAssignment.getFlightCrewMember().getIdentity().getFullName());

		SelectChoices dutyChoices = SelectChoices.from(DutyType.class, flightAssignment.getDuty());
		dataset.put("dutyChoices", dutyChoices);

		SelectChoices statusChoices = SelectChoices.from(Status.class, flightAssignment.getCurrentStatus());
		dataset.put("statusChoices", statusChoices);

		SelectChoices legChoices = SelectChoices.from(this.repository.findAllLegsFromAirline(flightAssignment.getFlightCrewMember().getAirline().getId()), "flightNumber", flightAssignment.getLeg());
		dataset.put("legChoices", legChoices);

		super.getResponse().addData(dataset);
	}

}
