
package acme.entities.leg;

import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
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
	private String				flightNumber;

	@Mandatory
	@Automapped
	private LocalTime			scheduledDeparture;

	@Mandatory
	@Automapped
	private LocalTime			scheduledArrival;

	@Mandatory
	@Automapped
	private Integer				duration;

	@Mandatory
	@Automapped
	private Status				status;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@OneToOne
	@Valid
	private Airport				departureAirport;

	@Mandatory
	@OneToOne
	@Valid
	private Airport				arraivalAirport;

	@Mandatory
	@OneToOne
	@Valid
	private Aircraft			aircraft;

}
