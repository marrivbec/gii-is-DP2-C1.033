
package acme.entities.booking;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.constraints.ValidLocatorCode;
import acme.entities.customer.Customer;
import acme.entities.flight.Flight;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Booking extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Column(unique = true)
	@ValidLocatorCode
	private String				locatorCode;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				purchaseMoment;

	@Mandatory
	@Automapped
	private TravelClass			travelClass;

	@Mandatory
	@Automapped
	private Double				price;

	@Optional
	@Automapped
	private String				creditCardLastNibble;

	// Relationships ----------------------------------------------------------

	@Mandatory
	@ManyToOne
	@Automapped
	private Customer			customer;

	@Mandatory
	@ManyToOne
	@Automapped
	private Flight				flight;

}
