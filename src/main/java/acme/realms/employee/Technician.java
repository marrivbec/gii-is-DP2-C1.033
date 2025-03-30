
package acme.realms.employee;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.constraints.ValidEmployeeCode;
import acme.constraints.ValidLongText;
import acme.constraints.ValidPhone;
import acme.constraints.ValidShortText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidEmployeeCode
public class Technician extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	//@ValidEmployeeCode
	@Column(unique = true)
	private String				employeeCode;

	@Mandatory
	@ValidPhone
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@ValidShortText
	@Automapped
	private String				specialisation;

	@Mandatory
	// @Valid by default
	@Automapped
	private boolean				passedAnnualHealthTest;

	@Mandatory
	@ValidNumber(min = 0, max = 120)
	@Automapped
	private Integer				experienceYears;

	@Optional
	@ValidLongText
	@Automapped
	private String				certifications;
}
