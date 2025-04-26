
package acme.features.customer.dashboard;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flight.Flight;
import acme.realms.client.Customer;

@Repository
public interface CustomerDashboardRepository extends AbstractRepository {

	@Query("select c from Customer c where c.id=:id")
	Customer findCustomer(int id);

	@Query("select f from Flight f where f.id=:id")
	Flight findById(int id);

	@Query("""
		    SELECT distinct b.flight
		    FROM Booking b
		    WHERE b.customer.id = :customerId AND b.flight.draftMode=false
		""")
	List<Flight> findFlightsByCustomerId(int customerId);

	default List<String> find5topFlightsByCustomerId(final int customerId) {
		List<Flight> reservas = this.findFlightsByCustomerId(customerId);
		List<String> vuelosOrdenados = reservas.stream().sorted(Comparator.comparing(Flight::getScheduledDeparture).reversed())  // Ordena en orden descendente por la fecha
			.map(x -> x.getDestinationCity()).distinct().limit(5)  // Limita a los 5 vuelos más recientes
			.collect(Collectors.toList());
		return vuelosOrdenados;// Ordena en orden descendente por la fecha de salida programada

	}
	//•	The money spent in bookings during the last year
	@Query("SELECT SUM(b.flight.cost.amount*(SELECT COUNT(br) FROM BookingRecord br WHERE br.booking = b)) FROM Booking b WHERE b.customer.id = :customerId AND b.purchaseMoment >= :dateLimit")
	Double moneySpentInBookingDuringLastYear(int customerId, Date dateLimit);

	//•	Their number of bookings grouped by travel class

	@Query("select c from Booking c where c.customer.id = :auditorId")
	Collection<Booking> findPublishedCodeAudits(int auditorId);

	default Map<TravelClass, Integer> totalTypes(final int customerId) {
		Map<TravelClass, Integer> result = new EnumMap<>(TravelClass.class);
		Collection<Booking> codeAudits = this.findPublishedCodeAudits(customerId);

		for (TravelClass type : TravelClass.values())
			result.put(type, 0);
		for (Booking c : codeAudits)
			result.put(c.getTravelClass(), result.get(c.getTravelClass()) + 1);
		return result;
	}

	@Query("SELECT COUNT(br) FROM BookingRecord br WHERE br.passenger.customer.id = :customer")
	Integer countPassengersByCustomer(int customer);

	@Query("SELECT AVG(select count(b) from BookingRecord b where b.booking.id=br.id) FROM Booking br WHERE br.customer.id = :customer ")
	Double averagePassengersByCustomer(int customer);

	@Query("SELECT MIN(select count(b) from BookingRecord b where b.booking.id=br.id) FROM Booking br WHERE br.customer.id = :customer ")
	Double minPassengersByCustomer(int customer);

	@Query("SELECT MAX(select count(b) from BookingRecord b where b.booking.id=br.id) FROM Booking br WHERE br.customer.id = :customer  ")
	Double maxPassengersByCustomer(int customer);

	@Query("SELECT COUNT(b) FROM Booking b WHERE b.customer = :customer AND b.purchaseMoment >= :dateLimit")
	Integer countBookingsInLastFiveYears(Customer customer, Date dateLimit);

	@Query("SELECT AVG(b.flight.cost.amount * (SELECT COUNT(br) FROM BookingRecord br WHERE br.booking = b)) FROM Booking b WHERE b.customer = :customer AND b.purchaseMoment >= :dateLimit")
	Double averageBookingCostInLastFiveYears(Customer customer, Date dateLimit);

	@Query("SELECT MIN(b.flight.cost.amount* (SELECT COUNT(br) FROM BookingRecord br WHERE br.booking = b)) FROM Booking b WHERE b.customer = :customer AND b.purchaseMoment >= :dateLimit")
	Double minBookingCostInLastFiveYears(Customer customer, Date dateLimit);

	@Query("SELECT MAX(b.flight.cost.amount*(SELECT COUNT(br) FROM BookingRecord br WHERE br.booking = b)) FROM Booking b WHERE b.customer = :customer AND b.purchaseMoment >= :dateLimit")
	Double maxBookingCostInLastFiveYears(Customer customer, Date dateLimit);

	@Query("SELECT STDDEV(b.flight.cost.amount) FROM Booking b WHERE b.customer = :customer AND b.purchaseMoment >= :dateLimit")
	Double stddevBookingCostInLastFiveYears(Customer customer, Date dateLimit);

	@Query("SELECT SQRT(SUM((b.flight.cost.amount*(SELECT COUNT(br) FROM BookingRecord br WHERE br.booking = b) - (SELECT AVG(b2.flight.cost.amount*(SELECT COUNT(br) FROM BookingRecord br WHERE br.booking = b)) FROM Booking b2 WHERE b2.customer = :customer AND b2.purchaseMoment >= :moment)) * (b.flight.cost.amount*(SELECT COUNT(br) FROM BookingRecord br WHERE br.booking = b) - (SELECT AVG(b2.flight.cost.amount*(SELECT COUNT(br) FROM BookingRecord br WHERE br.booking = b)) FROM Booking b2 WHERE b2.customer = :customer AND b2.purchaseMoment >= :moment))) / COUNT(b)) FROM Booking b WHERE b.customer = :customer AND b.purchaseMoment >= :moment")
	Double deviationBookingCost(Customer customer, Date moment);

}
