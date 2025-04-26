
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.datatypes.Statistics;
import acme.entities.booking.TravelClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {
	// Serialisation version --------------------------------------------------

	private static final long			serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private List<String>				theLastFiveDestinations;
	private Double						moneySpentInBookingDuringLastYear;
	private Map<TravelClass, Integer>	bookingsGroupedByTravelClass;

	private Statistics					booking5Years;

	private Statistics					passengersBooking;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
}
