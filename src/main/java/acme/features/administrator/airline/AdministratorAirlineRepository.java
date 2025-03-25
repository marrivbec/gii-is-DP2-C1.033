
package acme.features.administrator.airline;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airline.Airline;

@Repository
public interface AdministratorAirlineRepository extends AbstractRepository {

	@Query("select a from Airline a where a.id = :id")
	Airline findAirlineById(int id);

	@Query("select a from Airline a")
	Collection<Airline> findAllAirlines();

	@Query("select count(a) from Aircraft a where a.airline.id = :id")
	int countAircraftByAirlineId(int id);

	@Query("select count(a) from AssistanceAgent a where a.airline.id = :id")
	int countAssistanceAgentByAirlineId(int id);

}
