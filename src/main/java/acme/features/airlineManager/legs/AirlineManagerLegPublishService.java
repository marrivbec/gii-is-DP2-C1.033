
package acme.features.airlineManager.legs;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.aircraft.AircraftStatus;
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
		boolean status, status2;
		int legId;
		Aircraft aircraft;
		Airport airportArrival;
		Airport airportDeparture;
		Flight flight;
		String method;
		int aircraftId, airportDepartureId, airportArrivalId;
		method = super.getRequest().getMethod();
		Collection<Aircraft> myAircrafts;

		legId = super.getRequest().getData("id", int.class);
		Leg leg = this.repository.findLegById(legId);
		flight = this.repository.findFlightByLegid(legId);
		status = flight != null && leg.isDraftMode() && super.getRequest().getPrincipal().hasRealm(flight.getAirlineManager());

		if (method.equals("GET"))
			status2 = status;
		else {
			airportDepartureId = super.getRequest().getData("departureAirport", int.class);
			airportArrivalId = super.getRequest().getData("arrivalAirport", int.class);
			aircraftId = super.getRequest().getData("aircraft", int.class);

			myAircrafts = this.repository.findAircraftsByAirlineId(flight.getAirlineManager().getAirline().getId());
			airportArrival = this.repository.findAirportById(airportArrivalId);
			airportDeparture = this.repository.findAirportById(airportDepartureId);
			aircraft = this.repository.findAircraftById(aircraftId);
			status2 = (aircraftId == 0 || aircraft != null && myAircrafts.contains(aircraft)) && (airportDepartureId == 0 || airportDeparture != null) && (airportArrivalId == 0 || airportArrival != null);
		}

		super.getResponse().setAuthorised(status && status2);
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
		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "aircraft", "departureAirport", "arrivalAirport");
	}
	@Override
	public void validate(final Leg leg) {
		Collection<Leg> legs = this.repository.findLegsByMasterId(leg.getFlight().getId());
		Date departure = leg.getScheduledDeparture();
		Date arrival = leg.getScheduledArrival();
		boolean estado = true;
		boolean estadoTime = true;
		boolean diferenteAirport = true;
		boolean aircraftNotUsed = true;
		boolean timePast = true;
		if (leg.getAircraft() != null) {
			boolean isAircraftActive = leg.getAircraft().getStatus().equals(AircraftStatus.ACTIVE);
			super.state(isAircraftActive, "aircraft", "airlineManager.leg.error.aircraft-under-maintenance.message");
		}
		if (leg.getArrivalAirport() != null && leg.getDepartureAirport() != null && leg.getArrivalAirport().equals(leg.getDepartureAirport()))
			diferenteAirport = false;
		if (arrival != null && departure != null && arrival.equals(departure))
			estadoTime = false;
		if (leg.getScheduledArrival() != null && leg.getScheduledDeparture() != null && leg.getScheduledArrival().before(leg.getScheduledDeparture()))
			estadoTime = false;
		if (estadoTime && diferenteAirport)
			for (Leg otherLeg : legs)
				if (!otherLeg.equals(leg) && !otherLeg.isDraftMode()) {
					Date otherDeparture = otherLeg.getScheduledDeparture();
					Date otherArrival = otherLeg.getScheduledArrival();

					if (departure.before(otherArrival) && arrival.after(otherDeparture) || departure.equals(otherDeparture) || arrival.equals(otherArrival))	// Si las franjas horarias se solapan
						estado = false;
				}
		if (leg.getScheduledDeparture() != null && leg.getScheduledArrival() != null) {
			Date horaActual = MomentHelper.getCurrentMoment();
			timePast = leg.getScheduledDeparture().after(horaActual) && leg.getScheduledArrival().after(horaActual);
			if (leg.getAircraft() != null) {
				Integer numberOfLegsDeployingAircraft = this.repository.findNumberOfLegsSolapedAircraft(leg.getScheduledDeparture(), leg.getScheduledArrival(), Status.CANCELLED, leg.getAircraft().getId());
				aircraftNotUsed = leg.getStatus() == Status.CANCELLED || numberOfLegsDeployingAircraft == 0;
			}
		}

		super.state(diferenteAirport, "*", "airlineManager.leg.error.sameAirport.message");
		super.state(estado, "*", "airlineManager.leg.error.timesOverlap.message");
		super.state(estadoTime, "*", "airlineManager.leg.error.times.message");
		super.state(aircraftNotUsed, "*", "airlineManager.leg.error.aircraftSolaped.message");
		super.state(timePast, "*", "airlineManager.leg.error.timePast.message");

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

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "draftMode");
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
