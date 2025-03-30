
package acme.features.airlineManager.legs;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight.Flight;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;

@Repository
public interface AirlineManagerLegRepository extends AbstractRepository {

	@Query("select f from Flight f where f.id = :id")
	Flight findFlightById(int id);

	@Query("select l.flight from Leg l where l.flight.id = :id")
	Flight findFlightByLegid(int id);

	@Query("select l from Leg l where l.id = :id")
	Leg findLegById(int id);

	@Query("select l from Leg l where l.flight.id = :masterId")
	Collection<Leg> findLegsByMasterId(int masterId);

	@Query("select f from FlightAssignment f where f.leg.id=:id")
	Collection<FlightAssignment> findAllFlightAssignmentByLegId(int id);

}
