
package acme.entities.booking;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidLastNibble;
import acme.constraints.ValidLocatorCode;
import acme.entities.flight.Flight;
import acme.entities.passenger.PassengerRepository;
import acme.realms.client.Customer;
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
	@Valid
	@Automapped
	private TravelClass			travelClass;

	@Optional
	@Automapped
	@ValidLastNibble
	private String				lastNibble;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------


	@Transient
	public Money price() {
		if (this.flight == null)
			return null;
		Money money = new Money();
		PassengerRepository repository = SpringHelper.getBean(PassengerRepository.class);
		int numberOfPassengers = repository.countPassengerByBookingId(this.getId());
		Double newAmount = this.getFlight().getCost().getAmount() * numberOfPassengers;
		money.setAmount(newAmount);
		money.setCurrency(this.getFlight().getCost().getCurrency());
		return money;
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Customer	customer;

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Flight		flight;

}
