
package acme.features.airlineManager.legs;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.entities.leg.Status;
import acme.realms.employee.AirlineManager;

@GuiService
public class AirlineManagerLegShowService extends AbstractGuiService<AirlineManager, Leg> {

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
		status = flight != null && super.getRequest().getPrincipal().hasRealm(flight.getAirlineManager());

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
	public void unbind(final Leg leg) {
		SelectChoices choicesStatus, choicesAircraft, choicesArrivalAirport, choicesDepartureAirport;
		Collection<Aircraft> aircrafts;
		Collection<Airport> airportsA;
		Dataset dataset;
		Flight flight;
		flight = this.repository.findFlightByLegid(leg.getId());
		aircrafts = this.repository.findAircraftsByAirlineId(flight.getAirlineManager().getAirline().getId());
		airportsA = this.repository.findAllAirport();

		choicesStatus = SelectChoices.from(Status.class, leg.getStatus());
		choicesAircraft = SelectChoices.from(aircrafts, "registrationNumber", leg.getAircraft());
		choicesDepartureAirport = SelectChoices.from(airportsA, "name", leg.getDepartureAirport());
		choicesArrivalAirport = SelectChoices.from(airportsA, "name", leg.getArrivalAirport());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "draftMode");
		dataset.put("masterId", flight.getId());
		dataset.put("status", choicesStatus);
		dataset.put("aircrafts", choicesAircraft);
		dataset.put("departureAirports", choicesDepartureAirport);
		dataset.put("arrivalAirports", choicesArrivalAirport);
		super.getResponse().addData(dataset);
	}

}
