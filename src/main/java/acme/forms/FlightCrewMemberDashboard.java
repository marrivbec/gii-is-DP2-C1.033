
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.datatypes.Statistics;
import acme.entities.flightAssignment.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightCrewMemberDashboard extends AbstractForm {

	//	Serialisation identifier -----------------------------------------------

	protected static final long		serialVersionUID	= 1L;

	//	Attributes -------------------------------------------------------------

	private List<String>			lastFiveDestinations;

	private Integer					legsWithIncidentSeverity03;

	private Integer					legsWithIncidentSeverity47;

	private Integer					legsWithIncidentSeverity810;

	private List<String>			lastLegCrewMembers;

	private Map<Status, Integer>	flightAssignmentsGroupedByStatus;

	private Statistics				flightAssignmentsStatistics;

}
