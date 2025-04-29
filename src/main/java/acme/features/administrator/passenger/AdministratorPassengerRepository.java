
package acme.features.administrator.passenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.passenger.Passenger;

@Repository
public interface AdministratorPassengerRepository extends AbstractRepository {

	@Query("select b.passenger from BookingRecord b  WHERE b.booking.id = :bookingId")
	Collection<Passenger> findPassengersByBookingId(int bookingId);

	@Query("SELECT b from Booking b WHERE b.id=:id")
	Booking findBookingById(int id);

	@Query("SELECT p FROM Passenger p WHERE p.id = :id")
	Passenger findPassengerById(int id);
}
