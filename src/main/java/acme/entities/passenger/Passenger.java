
package acme.entities.passenger;

import java.time.LocalDate;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.constraints.ValidLongText;
import acme.constraints.ValidPassportNumber;
import acme.constraints.ValidShortText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Passenger extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidLongText
	@Automapped
	private String				fullName;

	@Mandatory
	@Automapped
	private String				email;

	@Mandatory
	@ValidPassportNumber
	@Automapped
	private String				passportNumber;

	@Mandatory
	@Automapped
	private LocalDate			dateOfBirth;

	@Optional
	@ValidShortText
	@Automapped
	private String				specialNeeds;

	// Relationships ----------------------------------------------------------

}
