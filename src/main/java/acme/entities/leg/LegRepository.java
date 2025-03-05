
package acme.entities.leg;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface LegRepository extends AbstractRepository {

	@Query("SELECT l.scheduledDeparture FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledDeparture ASC")
	List<LocalDateTime> findScheduledDepartureByFlightId(int flightId);

	@Query("SELECT l.scheduledArrival FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledArrival DESC")
	List<LocalDateTime> findScheduledArrivalByFlightId(int flightId);

	@Query("SELECT l.departureAirport.city FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledDeparture ASC")
	List<String> findOriginCityByFlightId(int flightId);

	@Query("SELECT l.arrivalAirport.city FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledArrival DESC")
	List<String> findDestinationCityByFlightId(int flightId);

	@Query("SELECT COUNT(l) - 1 FROM Leg l WHERE l.flight.id = :flightId")
	Integer findLayoversByFlightId(int flightId);
}
