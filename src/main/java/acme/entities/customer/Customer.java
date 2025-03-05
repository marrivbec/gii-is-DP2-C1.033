
package acme.entities.customer;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.constraints.ValidEarnedPoints;
import acme.constraints.ValidIdentifierNumber;
import acme.constraints.ValidLongText;
import acme.constraints.ValidPhone;
import acme.constraints.ValidShortText;
import acme.datatypes.Phone;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidIdentifierNumber
	@Column(unique = true)
	private String				identifier;

	@Optional
	@ValidPhone
	@Automapped
	private Phone				phoneNumber;

	@Mandatory
	@ValidLongText
	@Automapped
	private String				physicalAddress;

	@Mandatory
	@ValidShortText
	@Automapped
	private String				city;

	@Mandatory
	@ValidShortText
	@Automapped
	private String				country;

	@Optional
	@ValidEarnedPoints
	@Automapped
	private Integer				earnedPoints;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
