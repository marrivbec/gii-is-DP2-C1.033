
package acme.entities.flightAssignment;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.constraints.ValidLongText;
import acme.entities.leg.Leg;
import acme.realms.employee.FlightCrewMember;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

@Table(indexes = {
	@Index(columnList = "leg_id, duty"), @Index(columnList = "leg_id, duty, id")
})

public class FlightAssignment extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Valid
	@Automapped
	private DutyType			duty;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.DATE)
	private Date				moment;

	@Mandatory
	@Valid
	@Automapped
	private Status				currentStatus;

	@Optional
	@ValidLongText
	@Automapped
	private String				remarks;

	@Mandatory
	// HINT: @Valid by default.
	@Automapped
	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Leg					leg;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private FlightCrewMember	flightCrewMember;

}
