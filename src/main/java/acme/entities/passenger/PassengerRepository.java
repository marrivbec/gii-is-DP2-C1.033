
package acme.entities.passenger;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface PassengerRepository extends AbstractRepository {

	@Query("SELECT COUNT(r) FROM BookingRecord r WHERE r.booking.id = :bookingId")
	int countPassengerByBookingId(int bookingId);
}
