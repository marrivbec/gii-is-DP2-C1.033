
package acme.features.flightCrewMember.flightAssigment;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.DutyType;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flightAssignment.Status;
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

		Dataset dataset = super.unbindObject(flightAssignment, "duty", "moment", "currentStatus", "remarks", "flightCrewMember", "leg", "draftMode");

		dataset.put("flightCrewMember", flightAssignment.getFlightCrewMember().getIdentity().getFullName());

		SelectChoices dutyChoices = SelectChoices.from(DutyType.class, flightAssignment.getDuty());
		dataset.put("dutyChoices", dutyChoices);

		SelectChoices statusChoices = SelectChoices.from(Status.class, flightAssignment.getCurrentStatus());
		dataset.put("statusChoices", statusChoices);

		//		this.repository.findAllLegsFromAirline(flightAssignment.getFlightCrewMember().getAirline().getId()).stream().forEach(x -> System.out.println(x.getFlightNumber()));
		//
		//		System.out.println(this.repository.findAllLegsFromAirline(flightAssignment.getFlightCrewMember().getAirline().getId()).contains(flightAssignment.getLeg()));

		SelectChoices legChoices = SelectChoices.from(this.repository.findAllLegsFromAirline(flightAssignment.getFlightCrewMember().getAirline().getId()), "flightNumber", flightAssignment.getLeg());
		dataset.put("legChoices", legChoices);

		super.getResponse().addData(dataset);
	}

}
