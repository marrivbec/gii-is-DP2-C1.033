
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.passenger.Passenger;
import acme.realms.client.Customer;

public interface CustomerBookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.id = :id")
	Booking findBookingById(int id);

	@Query("select b from Booking b where b.customer.id= :id")
	Collection<Booking> findAllBookings(int id);

	@Query("select c from Customer c where c.id= :id")
	Customer findCustomerLogged(int id);

	@Query("SELECT r.passenger FROM BookingRecord r WHERE r.booking.id = :bookingId")
	Collection<Passenger> PassengerByBookingId(int bookingId);

}
