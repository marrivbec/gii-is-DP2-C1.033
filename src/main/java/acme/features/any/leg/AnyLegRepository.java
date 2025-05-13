
package acme.features.any.leg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;

@Repository
public interface AnyLegRepository extends AbstractRepository {

	@Query("select f from Flight f where f.draftMode = false")
	Collection<Flight> findAllFlightDraftMode();

	@Query("select f from Flight f where f.id = :id")
	Flight findFlightById(int id);

	@Query("select l from Leg l where l.id = :id")
	Leg findLegById(int id);

	@Query("select l.flight from Leg l where l.id = :id")
	Flight findFlightByLegid(int id);

	@Query("select l from Leg l where l.flight.id = :masterId and l.draftMode = false")
	Collection<Leg> findLegsByMasterId(int masterId);

}
