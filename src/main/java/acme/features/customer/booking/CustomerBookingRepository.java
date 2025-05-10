
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.flight.Flight;
import acme.entities.passenger.Passenger;
import acme.realms.client.Customer;

public interface CustomerBookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.id = :id")
	public Booking findBookingById(int id);

	@Query("select b from Booking b where b.customer.id= :id")
	public Collection<Booking> findAllBookings(int id);

	@Query("select c from Customer c where c.id= :id")
	public Customer findCustomerLogged(int id);

	@Query("SELECT r.passenger FROM BookingRecord r WHERE r.booking.id = :bookingId")
	public Collection<Passenger> passengerByBookingId(int bookingId);

	@Query("select b from Booking b where b.locatorCode =?1")
	public Collection<Booking> findAllBookingLocatorCode(String locatorCode);

	@Query("SELECT b.flight FROM Booking b WHERE b.id=:bookingId")
	public Flight findFlightByBookingId(int bookingId);

	@Query("SELECT f FROM Flight f where f.draftMode = false")
	public Collection<Flight> allFlight();

	@Query("select f from Flight f where f.id=:id")
	public Flight findFlightById(int id);

	@Query("select r from BookingRecord r where r.booking.id=:id")
	public Collection<BookingRecord> findBookingRById(int id);
}
