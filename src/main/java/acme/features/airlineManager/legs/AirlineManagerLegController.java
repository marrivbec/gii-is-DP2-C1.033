
package acme.features.airlineManager.legs;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.leg.Leg;
import acme.realms.employee.AirlineManager;

@GuiController
public class AirlineManagerLegController extends AbstractGuiController<AirlineManager, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerLegListService	listService;

	@Autowired
	private AirlineManagerLegShowService	showService;

	@Autowired
	private AirlineManagerLegCreateService	createService;

	@Autowired
	private AirlineManagerLegUpdateService	updateService;

	@Autowired
	private AirlineManagerLegDeleteService	deleteService;

	@Autowired
	private AirlineManagerLegPublishService	publishService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("publish", "update", this.publishService);
	}

}
