
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.passenger.Passenger;
import acme.realms.client.Customer;

@Repository
public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("select p from Passenger p where p.customer.id=:id")
	public Collection<Passenger> passengerByCustomer(int id);

	@Query("select p from Passenger p where p.id=:id")
	public Passenger findPassengerById(int id);

	@Query("select c from Customer c where c.id=:id")
	public Customer findCustomerById(int id);

	@Query("SELECT b from Booking b WHERE b.id=:id")
	public Booking findBookingById(int id);

	@Query("select r from BookingRecord r where r.passenger.id=:id")
	public Collection<BookingRecord> findBookingRByPassengerId(int id);

}
