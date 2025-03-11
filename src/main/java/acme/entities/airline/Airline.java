
package acme.entities.airline;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidIATA;
import acme.constraints.ValidPhone;
import acme.constraints.ValidShortText;
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
	@Column(unique = true)
	@ValidIATA
	private String				iata;

	@Mandatory
	@Automapped
	@ValidUrl
	private String				web;

	@Mandatory
	@Valid
	@Automapped
	private AirlineType			type;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.DATE)
	private Date				dateFundation;

	@Optional
	@Automapped
	@ValidEmail
	private String				mail;

	@Optional
	@Automapped
	@ValidPhone
	private String				phone;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
