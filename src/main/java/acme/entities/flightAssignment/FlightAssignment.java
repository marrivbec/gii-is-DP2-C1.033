
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
import acme.client.components.validation.ValidString;
import acme.entities.leg.Leg;
import acme.realms.employee.FlightCrewMember;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

@Table(indexes = {

	// Este sería el único índice apropiado de las consultas de FlightAssignmentRepository, proviene de: 
	// @Query("SELECT COUNT(flightAssig) > 0 FROM FlightAssignment flightAssig WHERE flightAssig.leg.id = :legId AND (flightAssig.duty= acme.entities.flightAssignment.DutyType.COPILOT OR flightAssig.duty= acme.entities.flightAssignment.DutyType.PILOT) AND flightAssig.duty = :duty AND flightAssig.id != :id")
	// Aunque sería un indice apropiado para poner, tras analizarlo en DBeaver observamos que no mejora excesivamente el rendimiento tras su uso, ya que al filtrar previamente por leg_id se reduce
	// el número de flightAssignment significativamente.

	@Index(columnList = "leg_id, duty, id"),

	// Este índice proviene de la consulta: @Query("SELECT fa FROM FlightAssignment fa WHERE fa.draftMode = false")
	// Aunque sea del requisito suplementario 19, tras analizarlo en DBeaver, vemos que mejora significativamente el rendimiento pasando de tipo ALL a REF

	@Index(columnList = "draftMode"),

	// Este índice proviene de la consulta: @Query("SELECT COUNT(fa) FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :flightCrewMemberId AND fa.currentStatus = :status")
	// Aunque este índice sea del requisito suplementario 15, tras analizarlo en DBeaver, vemos que la columna "rows" disminuye (8 sin índice, 6 con índice), pero no significativamente.

	@Index(columnList = "flight_crew_member_id, currentStatus"),

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
	@ValidString(max = 255)
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
