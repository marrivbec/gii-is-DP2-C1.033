
package acme.features.customer.dashboard;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.Statistics;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.forms.CustomerDashboard;
import acme.realms.client.Customer;

@GuiService
public class CustomerDashboardShowService extends AbstractGuiService<Customer, CustomerDashboard> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);

	}
	@Override
	public void load() {

		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		CustomerDashboard dashboard;
		int id = customer.getId();
		Date moment;
		moment = MomentHelper.getCurrentMoment();
		LocalDate localDateMoment = moment.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate dateLimit = localDateMoment.minusYears(1);

		LocalDate dateLimit2 = localDateMoment.minusYears(5);

		Date date = Date.from(dateLimit.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date date2 = Date.from(dateLimit2.atStartOfDay(ZoneId.systemDefault()).toInstant());

		List<String> theLastFiveDestinations;

		Double moneySpentInBookingDuringLastYear;

		Map<TravelClass, Integer> bookingsGroupedByTravelClass;
		Collection<Booking> listaBooking = this.repository.findPublishedCodeAudits(customer.getId());

		Integer countPassenger;
		Double averagePassenger;
		Double minPassenger;
		Double maxPassenger;
		Double stddevPassenger;

		Integer countBooking;
		Double averageBooking;
		Double minBooking;
		Double maxBooking;
		Double stddevBooking;

		theLastFiveDestinations = this.repository.find5topFlightsByCustomerId(customer.getId());
		moneySpentInBookingDuringLastYear = this.repository.moneySpentInBookingDuringLastYear(id, date);
		bookingsGroupedByTravelClass = this.repository.totalTypes(id);

		countPassenger = this.repository.countPassengersByCustomer(id) != null ? this.repository.countPassengersByCustomer(id) : 0;
		averagePassenger = this.repository.averagePassengersByCustomer(id) != null ? this.repository.averagePassengersByCustomer(id) : 0;
		minPassenger = this.repository.minPassengersByCustomer(id) != null ? this.repository.minPassengersByCustomer(id) : 0.0;
		maxPassenger = this.repository.maxPassengersByCustomer(id) != null ? this.repository.maxPassengersByCustomer(id) : 0.0;

		stddevPassenger = this.calculateDeviation(id) != null ? this.calculateDeviation(id) : 0.0;
		countBooking = this.repository.countBookingsInLastFiveYears(customer, date2) != null ? this.repository.countBookingsInLastFiveYears(customer, date2) : 0;
		averageBooking = this.repository.averageBookingCostInLastFiveYears(customer, date2) != null ? this.repository.averageBookingCostInLastFiveYears(customer, date2) : 0;
		minBooking = this.repository.minBookingCostInLastFiveYears(customer, date2) != null ? this.repository.minBookingCostInLastFiveYears(customer, date2) : 0.0;
		maxBooking = this.repository.maxBookingCostInLastFiveYears(customer, date2) != null ? this.repository.maxBookingCostInLastFiveYears(customer, date2) : 0.0;
		stddevBooking = this.repository.deviationBookingCost(customer, date2) != null ? this.repository.stddevBookingCostInLastFiveYears(customer, date2) : 0.0;

		Statistics statPassenger = new Statistics();
		statPassenger.setCount(countPassenger);
		statPassenger.setAverage(averagePassenger);
		statPassenger.setMaximum(maxPassenger);
		statPassenger.setMinimum(minPassenger);
		statPassenger.setDeviation(stddevPassenger);

		Statistics statBooking = new Statistics();
		statBooking.setCount(countBooking);
		statBooking.setAverage(averageBooking);
		statBooking.setMaximum(maxBooking);
		statBooking.setMinimum(minBooking);
		statBooking.setDeviation(stddevBooking);

		dashboard = new CustomerDashboard();
		dashboard.setTheLastFiveDestinations(theLastFiveDestinations);
		dashboard.setMoneySpentInBookingDuringLastYear(moneySpentInBookingDuringLastYear);
		dashboard.setBookingsGroupedByTravelClass(bookingsGroupedByTravelClass);
		dashboard.setBooking5Years(statBooking);
		dashboard.setPassengersBooking(statPassenger);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final CustomerDashboard object) {
		Dataset dataset;
		Integer totalNumBUSINESS;
		Integer totalNumEconomy;
		String res5LastCity = String.join(" ", object.getTheLastFiveDestinations());
		totalNumBUSINESS = object.getBookingsGroupedByTravelClass().get(TravelClass.BUSINESS) != null ? object.getBookingsGroupedByTravelClass().get(TravelClass.BUSINESS) : 0;
		totalNumEconomy = object.getBookingsGroupedByTravelClass().get(TravelClass.ECONOMY) != null ? object.getBookingsGroupedByTravelClass().get(TravelClass.ECONOMY) : 0;

		dataset = super.unbindObject(object, //
			"moneySpentInBookingDuringLastYear",// 
			"booking5Years", "passengersBooking");
		dataset.put("totalNumTravelClassEconomy", totalNumEconomy);
		dataset.put("totalNumTravelClassBusiness", totalNumBUSINESS);
		dataset.put("theLastFiveDestinations", res5LastCity);

		super.getResponse().addData(dataset);
	}

	public Double calculateDeviation(final int customer) {

		Integer totalPassengers = this.repository.countPassengersByCustomer(customer);

		Double averagePassengers = this.repository.averagePassengersByCustomer(customer);

		Double minPassengers = this.repository.minPassengersByCustomer(customer);

		Double maxPassengers = this.repository.maxPassengersByCustomer(customer);

		double variance = 0.0;

		for (int i = minPassengers.intValue(); i <= maxPassengers.intValue(); i++) {
			Double diff = i - averagePassengers;
			variance += Math.pow(diff, 2);
		}

		Double deviation = Math.sqrt(variance / totalPassengers);

		return deviation;
	}

}
