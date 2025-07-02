
package acme.features.airlineManager.flights;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.employee.AirlineManager;

@GuiService
public class AirlineManagerFlightDeleteService extends AbstractGuiService<AirlineManager, Flight> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerFlightRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Flight flight;
		AirlineManager manager;

		masterId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(masterId);
		manager = flight == null ? null : flight.getAirlineManager();
		status = super.getRequest().getPrincipal().hasRealm(manager) && (flight == null || flight.isDraftMode());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Flight flight;
		int id;

		id = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(id);

		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
		super.bindObject(flight, "tag", "selfTransfer", "cost", "description");

	}

	@Override
	public void validate(final Flight flight) {
		Collection<Leg> legs;
		boolean confirmation;
		legs = this.repository.findAllLegByFlightId(flight.getId());
		if (!legs.isEmpty())
			confirmation = legs.stream().allMatch(leg -> leg.isDraftMode());
		else
			confirmation = true;
		super.state(confirmation, "*", "airlineManager.flight.error.deleteLegsPublish.message");// Comprobamos que todas las legs esten publicadas
	}

	@Override
	public void perform(final Flight flight) {
		Collection<FlightAssignment> flightAssignments;
		Collection<Leg> legs;

		legs = this.repository.findAllLegByFlightId(flight.getId());
		if (!legs.isEmpty())
			this.repository.deleteAll(legs);

		this.repository.delete(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;
		dataset = super.unbindObject(flight, "tag", "selfTransfer", "cost", "description", "draftMode");
		dataset.put("originCity", flight.getOriginCity());
		dataset.put("destinationCity", flight.getDestinationCity());
		dataset.put("scheduledDeparture", flight.getScheduledDeparture());
		dataset.put("scheduledArrival", flight.getScheduledArrival());
		dataset.put("layovers", flight.getLayovers());

		super.getResponse().addData(dataset);
	}
}
