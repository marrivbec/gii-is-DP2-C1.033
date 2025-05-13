
package acme.features.authenticated.airlineManager;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Authenticated;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.realms.employee.AirlineManager;

@GuiController
public class AuthenticatedAirlineManagerController extends AbstractGuiController<Authenticated, AirlineManager> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedAirlineManagerCreateService	createService;

	@Autowired
	private AuthenticatedAirlineManagerUpdateService	updateService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
	}
}
