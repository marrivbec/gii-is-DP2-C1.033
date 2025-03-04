
package acme.entities.aircraft;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.constraints.ValidEmployeeCode;
import acme.constraints.ValidLongText;
import acme.constraints.ValidPhone;
import acme.datatypes.Phone;
import acme.entities.airline.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class FlightCrewMember extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Column(unique = true)
	@ValidEmployeeCode
	private String				employee_code;

	@Mandatory
	@ValidPhone
	@Automapped
	private Phone				phone_number;

	@Mandatory
	@ValidLongText
	@Automapped
	private String				language_skills;

	@Mandatory
	@Automapped
	private AvailabilityStatus	availability_status;

	@Mandatory
	@ManyToOne
	@Automapped
	private Airline				airline;

	@Mandatory
	@Automapped
	private Integer				salary;

	@Optional
	@Automapped
	private Integer				years_of_experience;

}
