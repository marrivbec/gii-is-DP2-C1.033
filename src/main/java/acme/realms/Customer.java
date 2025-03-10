
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.constraints.ValidEarnedPoints;
import acme.constraints.ValidEmployeeCode;
import acme.constraints.ValidLongText;
import acme.constraints.ValidPhone;
import acme.constraints.ValidShortText;
import acme.datatypes.Phone;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidEmployeeCode
	@Column(unique = true)
	private String				identifier;

	@Mandatory
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
	@Transient
	public String initials() {
		return this.identifier.substring(0, this.identifier.length() - 6);
	}
	// Relationships ----------------------------------------------------------

}
