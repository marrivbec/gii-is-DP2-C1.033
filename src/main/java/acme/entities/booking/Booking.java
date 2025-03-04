
package acme.entities.booking;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.constraints.ValidPastDate;
import acme.entities.customer.Customer;
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
	private String				locatorCode;

	@Mandatory
	@ValidPastDate
	@Automapped
	private LocalDateTime		purchaseMoment;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private TravelClass			travelClass;

	@Mandatory
	@DecimalMin(value = "0.0", inclusive = true, message = "Price must be a positive value")
	@Automapped
	private Double				price;

	@Optional
	@Pattern(regexp = "^\\d{4}$", message = "Invalid credit card nibble format")
	@Automapped
	private String				creditCardNibble;

	// Relationships ----------------------------------------------------------

	@Mandatory
	@ManyToOne
	@Automapped
	private Customer			customer;

}
