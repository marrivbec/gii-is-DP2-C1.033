
package acme.features.flightCrewMember.flightAssigment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.employee.FlightCrewMember;

@GuiController
public class FlightCrewMemberFlightAssigmentController extends AbstractGuiController<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberFlightAssigmentListCompletedService	listCompleted;

	@Autowired
	private FlightCrewMemberFlightAssigmentListPlannedService	listPlanned;

	@Autowired
	private FlightCrewMemberFlightAssigmentShowService			showService;

	@Autowired
	private FlightCrewMemberFlightAssigmentCreateService		createService;

	@Autowired
	private FlightCrewMemberFlightAssigmentUpdateService		updateService;

	@Autowired
	private FlightCrewMemberFlightAssigmentPublishService		publishService;

	@Autowired
	private FlightCrewMemberFlightAssigmentDeleteService		deleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addCustomCommand("list-completed", "list", this.listCompleted);
		super.addCustomCommand("list-planned", "list", this.listPlanned);

		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);

		super.addCustomCommand("publish", "update", this.publishService);
		super.addBasicCommand("delete", this.deleteService);
	}
}
