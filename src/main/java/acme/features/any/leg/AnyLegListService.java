
package acme.features.any.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;

@GuiService
public class AnyLegListService extends AbstractGuiService<Any, Leg> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyLegRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Leg> legs;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		legs = this.repository.findLegsByMasterId(masterId);
		super.getBuffer().addData(legs);

	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "draftMode");
		super.addPayload(dataset, leg, "departureAirport.name", "arrivalAirport.name");

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<Leg> legs) {
		int masterId;
		Flight flight;
		final boolean showCreate;

		masterId = super.getRequest().getData("masterId", int.class);
		flight = this.repository.findFlightById(masterId);
		showCreate = flight.isDraftMode() && super.getRequest().getPrincipal().hasRealm(flight.getAirlineManager());

		super.getResponse().addGlobal("masterId", masterId);
		super.getResponse().addGlobal("showCreate", showCreate);
	}

}
