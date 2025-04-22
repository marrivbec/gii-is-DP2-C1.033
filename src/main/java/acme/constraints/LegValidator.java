
package acme.constraints;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.StringHelper;
import acme.entities.leg.Leg;
import acme.entities.leg.LegRepository;

public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	// Internal State ----------------------------------------------------

	@Autowired
	private LegRepository repository;

	// Initialiser -------------------------------------------------------


	@Override
	public void initialise(final ValidLeg annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (leg == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			if (leg.getScheduledArrival() != null && leg.getScheduledDeparture() != null) {
				boolean arrivalIsAfterDeparture;
				Date minMoment = MomentHelper.deltaFromMoment(leg.getScheduledDeparture(), 1, ChronoUnit.MINUTES);

				arrivalIsAfterDeparture = MomentHelper.isAfterOrEqual(leg.getScheduledArrival(), minMoment);
				super.state(context, arrivalIsAfterDeparture, "scheduledArrival", "acme.validation.leg.arrival.message");
			}

			if (leg.getFlightNumber() != null) {

				if (leg.getAircraft() != null) {
					boolean validIATA;
					String iataFromAirline = leg.getAircraft().getAirline().getIata();
					validIATA = StringHelper.startsWith(leg.getFlightNumber(), iataFromAirline, false);
					super.state(context, validIATA, "flightNumber", "acme.validation.leg.flightnumber.message");
				}

				boolean uniqueFlightNumber;
				Leg duplicatesLeg;

				duplicatesLeg = this.repository.findLegByFlightNumber(leg.getFlightNumber());
				uniqueFlightNumber = duplicatesLeg == null || duplicatesLeg.equals(leg);
				super.state(context, uniqueFlightNumber, "flightNumber", "acme.validation.leg.flightnumber.duplicated.message");
			}

			if (leg.getArrivalAirport() != null && leg.getDepartureAirport() != null) {
				boolean differentAirports;
				differentAirports = !leg.getDepartureAirport().equals(leg.getArrivalAirport());
				super.state(context, differentAirports, "departureAirport", "acme.validation.leg.airport.sameAirport");
				super.state(context, differentAirports, "arrivalAirport", "acme.validation.leg.airport.sameAirport");
			}

		}

		result = !super.hasErrors(context);
		return result;

	}
}
