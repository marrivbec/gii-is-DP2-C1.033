
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
public class AirlineManagerLegCreateService extends AbstractGuiService<AirlineManager, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerLegRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Flight flight;

		masterId = super.getRequest().getData("masterId", int.class);
		flight = this.repository.findFlightById(masterId);
		status = flight != null && flight.isDraftMode() && super.getRequest().getPrincipal().hasRealm(flight.getAirlineManager());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int masterId;
		Flight flight;

		masterId = super.getRequest().getData("masterId", int.class);
		flight = this.repository.findFlightById(masterId);

		leg = new Leg();
		leg.setDraftMode(true);
		leg.setFlight(flight);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "aircraft", "departureAirport", "arrivalAirport");
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
		SelectChoices choicesStatus, choicesAircraft, choicesArrivalAirport, choicesDepartureAirport;
		Collection<Aircraft> aircrafts;
		Collection<Airport> airportsD, airportsA;
		Dataset dataset;
		Flight flight;
		Collection<Airport> airportsArrival;
		Collection<Airport> airportsDeparture;

		int masterId = super.getRequest().getData("masterId", int.class);
		flight = this.repository.findFlightById(masterId);
		aircrafts = this.repository.findAircraftsByAirlineId(flight.getAirlineManager().getAirline().getId());
		airportsD = this.repository.findAllAirport();
		airportsA = this.repository.findAllAirport();
		airportsDeparture = this.repository.findDepartureAircraftsByFlightId(masterId);
		airportsArrival = this.repository.findArrivalAircraftsByFlightId(masterId);

		//Quitamos los airports de los que ya has salido y a los que ya has llegado de ambos choices

		airportsA.removeAll(airportsArrival);
		airportsD.removeAll(airportsDeparture);

		choicesStatus = SelectChoices.from(Status.class, leg.getStatus());
		choicesAircraft = SelectChoices.from(aircrafts, "registrationNumber", leg.getAircraft());
		choicesDepartureAirport = SelectChoices.from(airportsD, "name", leg.getDepartureAirport());
		choicesArrivalAirport = SelectChoices.from(airportsA, "name", leg.getArrivalAirport());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "draftMode");
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));
		dataset.put("status", choicesStatus);
		dataset.put("aircraft", choicesAircraft.getSelected().getKey());
		dataset.put("aircrafts", choicesAircraft);
		dataset.put("departureAirport", choicesDepartureAirport.getSelected().getKey());
		dataset.put("departureAirports", choicesDepartureAirport);
		dataset.put("arrivalAirport", choicesArrivalAirport.getSelected().getKey());
		dataset.put("arrivalAirports", choicesArrivalAirport);
		super.getResponse().addData(dataset);
	}

}
