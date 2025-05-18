
package acme.features.authenticated.flightCrewMember;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.airline.Airline;
import acme.realms.employee.FlightCrewMember;

@Repository
public interface AuthenticatedFlightCrewMemberRepository extends AbstractRepository {

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findUserAccountById(int id);

	@Query("select fcm from FlightCrewMember fcm where fcm.userAccount.id = :id")
	FlightCrewMember findFlightCrewMemberByUserAccountId(int id);

	@Query("select fcm from FlightCrewMember fcm where fcm.employeeCode =?1")
	Collection<FlightCrewMember> findFligthCrewMemberByCode(String locatorCode);

	@Query("SELECT a FROM Airline a")
	Collection<Airline> findAllAirlines();

	@Query("select a from Airline a where a.id = :id")
	Airline findAirlineById(int id);

}
