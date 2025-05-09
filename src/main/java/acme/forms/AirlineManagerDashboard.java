
package acme.forms;

import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.datatypes.Statistics;
import acme.entities.airport.Airport;
import acme.entities.leg.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AirlineManagerDashboard extends AbstractForm {
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	int							experienceRank;
	int							yearsUntilRetirement;
	double						flightTimelinessRatio;
	Airport						mostPopularAirport;
	Airport						leastPopularAirport;
	Map<Status, Long>			numberOfLegsByStatus;
	Statistics					costStatistics;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
