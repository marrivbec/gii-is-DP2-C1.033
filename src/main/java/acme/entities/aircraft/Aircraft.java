
package acme.entities.aircraft;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.constraints.ValidCargoWeight;
import acme.constraints.ValidShortText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Aircraft extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidShortText
	@Automapped
	private String				model;

	@Mandatory
	@Column(unique = true)
	@Automapped
	private String				registrationNumber;

	@Mandatory
	@Automapped
	private Integer				capacity;

	@Mandatory
	@ValidCargoWeight
	@Automapped
	private Integer				cargoWeight;

	@Mandatory
	@Automapped
	private AircraftStatus		status;

	@Optional
	@Automapped
	private String				details;

}
