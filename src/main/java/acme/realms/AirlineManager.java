
package acme.entities.airlineManager;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.constraints.ValidIdentifierNumber;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AirlineManager extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Column(unique = true)
	@ValidIdentifierNumber
	private String				numberID;

	@Mandatory
	@Automapped
	private Integer				yearsExp;

	@Mandatory
	private LocalDate			dateBirth;

	@Optional
	private String				urlImage;
}
