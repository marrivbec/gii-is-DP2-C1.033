
package acme.features.airlineManager.legs;

import java.util.Collection;
import java.util.Date;

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
public class AirlineManagerLegPublishService extends AbstractGuiService<AirlineManager, Leg> {

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
		Leg leg = this.repository.findLegById(legId);
		flight = this.repository.findFlightByLegid(legId);
		status = flight != null && leg.isDraftMode() && super.getRequest().getPrincipal().hasRealm(flight.getAirlineManager());

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
		super.bindObject(leg, "flightNumberDigits", "scheduledDeparture", "scheduledArrival", "status", "aircraft", "departureAirport", "arrivalAirport");
	}
	@Override
	public void validate(final Leg leg) {
		Collection<Leg> legs = this.repository.findLegsByMasterId(leg.getFlight().getId());
		Date departure = leg.getScheduledDeparture();
		Date arrival = leg.getScheduledArrival();
		boolean estado = true;
		boolean estadoTime = true;
		boolean diferenteAirport = true;
		if (leg.getAircraft() != null) {
			boolean isAircraftActive = leg.getAircraft().isStatus();
			super.state(isAircraftActive, "aircraft", "acme.validation.flight.aircraft-under-maintenance.message");
		}
		if (leg.getArrivalAirport().equals(leg.getDepartureAirport()))
			diferenteAirport = false;
		if (arrival.equals(departure))
			estadoTime = false;
		if (leg.getScheduledArrival().before(leg.getScheduledDeparture()))
			estadoTime = false;
		if (estadoTime && diferenteAirport)
			for (Leg otherLeg : legs)
				if (!otherLeg.equals(leg)) {
					Date otherDeparture = otherLeg.getScheduledDeparture();
					Date otherArrival = otherLeg.getScheduledArrival();

					if (departure.before(otherArrival) && arrival.after(otherDeparture) || departure.equals(otherDeparture) || arrival.equals(otherArrival))	// Si las franjas horarias se solapan
						estado = false;
				}
		super.state(diferenteAirport, "*", "airlineManager.leg.error.sameAirport.message");
		super.state(estado, "*", "airlineManager.leg.error.timesOverlap.message");
		super.state(estadoTime, "*", "airlineManager.leg.error.times.message");

	}

	@Override
	public void perform(final Leg leg) {
		leg.setDraftMode(false);
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		SelectChoices choicesStatus, choicesAircraft, choicesArrivalAirport, choicesDepartureAirport;
		Collection<Aircraft> aircrafts;
		Collection<Airport> airports;
		Dataset dataset;
		Flight flight;

		flight = leg.getFlight();
		aircrafts = this.repository.findAircraftsByAirlineId(flight.getAirlineManager().getAirline().getId());
		airports = this.repository.findAllAirport();

		choicesStatus = SelectChoices.from(Status.class, leg.getStatus());
		choicesAircraft = SelectChoices.from(aircrafts, "registrationNumber", leg.getAircraft());
		choicesDepartureAirport = SelectChoices.from(airports, "name", leg.getDepartureAirport());
		choicesArrivalAirport = SelectChoices.from(airports, "name", leg.getArrivalAirport());

		dataset = super.unbindObject(leg, "flightNumberDigits", "scheduledDeparture", "scheduledArrival", "draftMode");
		dataset.put("flightNumber", leg.getFlightNumber());
		dataset.put("masterId", flight.getId());
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
