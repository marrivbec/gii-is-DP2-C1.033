
package acme.features.any.flightAssignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;

@Repository
public interface AnyFlightAssignmentRepository extends AbstractRepository {

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.draftMode = false")
	Collection<FlightAssignment> findAllPublishedFA();

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.id = :flightAssignmentId")
	FlightAssignment findFlightAssignmentById(int flightAssignmentId);

	@Query("SELECT l FROM Leg l WHERE l.aircraft.airline.id = :airlineId")
	Collection<Leg> findAllLegsByAirlineId(int airlineId);

}
