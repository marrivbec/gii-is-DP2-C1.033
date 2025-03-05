
package acme.entities.leg;

import java.time.Duration;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.constraints.ValidFlightNumber;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Leg extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Automapped
	@Column(unique = true)
	@ValidFlightNumber
	private String				flightNumber;

	@Mandatory
	@Automapped
	private LocalDateTime		scheduledDeparture;

	@Mandatory
	@Automapped
	private LocalDateTime		scheduledArrival;

	@Mandatory
	@Automapped
	private Duration			duration;

	@Mandatory
	@Automapped
	private Status				status;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@ManyToOne
	@Valid
	private Airport				departureAirport;

	@Mandatory
	@ManyToOne
	@Valid
	private Airport				arrivalAirport;

	@Mandatory
	@ManyToOne
	@Valid
	private Aircraft			aircraft;

	@ManyToOne
	@JoinColumn(name = "flight_id", nullable = false)
	private Flight				flight;

}
