
package acme.realms.employee;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidEmployeeCode;
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
	@ValidEmployeeCode
	private String				numberID;

	@Mandatory
	@Automapped
	@ValidNumber(min = 0)
	private Integer				yearsExp;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.DATE)
	private Date				dateBirth;

	@Optional
	@ValidUrl
	@Automapped
	private String				urlImage;
}
