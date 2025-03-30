
package acme.features.airlineManager.legs;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.entities.leg.Status;
import acme.realms.employee.AirlineManager;

@GuiService
public class AirlineManagerLegUpdateService extends AbstractGuiService<AirlineManager, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerLegRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int legId;
		Flight flight;

		legId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightByLegid(legId);
		status = flight != null && flight.isDraftMode() && super.getRequest().getPrincipal().hasRealm(flight.getAirlineManager());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(id);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status");
	}

	@Override
	public void validate(final Leg leg) {
		;
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		SelectChoices choices;
		Dataset dataset;
		choices = SelectChoices.from(Status.class, leg.getStatus());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival");
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));
		dataset.put("draftMode", leg.getFlight().isDraftMode());
		dataset.put("status", choices);

		super.getResponse().addData(dataset);
	}

}
