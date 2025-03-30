
package acme.features.flightCrewMember.flightAssigment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.leg.Leg;

@Repository
public interface FlightCrewMemberFlightAssigmentRepository extends AbstractRepository {

	@Query("SELECT l FROM Leg l")
	Collection<Leg> findAllLeg();

}
