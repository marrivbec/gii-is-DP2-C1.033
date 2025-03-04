
package acme.entities.airport;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidIATA;
import acme.constraints.ValidPhone;
import acme.constraints.ValidShortText;
import acme.datatypes.Phone;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Airport extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidShortText
	@Automapped
	private String				name;

	@Mandatory
	@ValidIATA
	@Automapped
	private String				IATA_code;

	@Mandatory
	@Automapped
	private OperationalScope	operational_scope;

	@Mandatory
	@ValidShortText
	@Automapped
	private String				city;

	@Mandatory
	@ValidShortText
	@Automapped
	private String				country;

	@Mandatory
	@ValidUrl
	@Automapped
	private String				website;

	@Mandatory
	@ValidShortText
	@Automapped
	private String				email_address;

	@Mandatory
	@ValidPhone
	@Automapped
	private Phone				contact_phone_number;

}
