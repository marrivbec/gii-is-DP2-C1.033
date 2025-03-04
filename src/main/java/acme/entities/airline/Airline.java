
package acme.entities.airline;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
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
public class Airline extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Automapped
	@ValidShortText
	private String				name;

	@Mandatory
	@Automapped
	@ValidIATA
	private String				iata;

	@Mandatory
	@Automapped
	@ValidUrl
	private String				web;

	@Mandatory
	@Automapped
	private AirlineType			type;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.DATE)
	private Date				dateFundation;

	@Optional
	@Automapped
	private String				mail;

	@Optional
	@Automapped
	@ValidPhone
	private Phone				phone;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
