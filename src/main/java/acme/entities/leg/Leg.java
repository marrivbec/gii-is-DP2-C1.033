
package acme.entities.leg;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.constraints.ValidFlightNumber;
import acme.constraints.ValidLeg;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

@ValidLeg
@Table(indexes = {

	@Index(columnList = "draftMode, status, scheduledArrival, scheduledDeparture"), @Index(columnList = "draftMode, scheduledDeparture"), @Index(columnList = "scheduledArrival, draftMode")

})
public class Leg extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Column(unique = true)
	@ValidFlightNumber
	private String				flightNumber;

	@Mandatory
	@ValidMoment()
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment()
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@Mandatory
	@Valid
	@Automapped
	private Status				status;

	@Mandatory
	// HINT: @Valid by default.
	@Automapped
	private boolean				draftMode;


	// Derived attributes -----------------------------------------------------
	@Transient
	public int getDurationInHours() {
		Instant departureInstant = this.scheduledDeparture.toInstant();
		Instant arrivalInstant = this.scheduledDeparture.toInstant();

		Duration duration = Duration.between(departureInstant, arrivalInstant);

		return duration.toHoursPart();
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Airport		departureAirport;

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Airport		arrivalAirport;

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Aircraft	aircraft;

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Flight		flight;

}
