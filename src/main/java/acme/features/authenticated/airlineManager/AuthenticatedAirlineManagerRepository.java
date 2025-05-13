
package acme.features.authenticated.airlineManager;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.airline.Airline;
import acme.realms.client.Customer;
import acme.realms.employee.AirlineManager;

@Repository
public interface AuthenticatedAirlineManagerRepository extends AbstractRepository {

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findUserAccountById(int id);

	@Query("select c from Customer c where c.userAccount.id = :id")
	Customer findCustomerByUserAccountId(int id);

	@Query("select a from Airline a")
	Collection<Airline> findAllAirlines();

	@Query("select a from AirlineManager a where a.employeeCode =?1")
	Collection<AirlineManager> findAirlineManagerByCode(String cod);

	@Query("select a from AirlineManager a where a.userAccount.id = :id")
	AirlineManager findOneAirlineManagerByUserAccountId(int id);

	@Query("select a from Airline a where a.id = :id")
	Airline findAirlineById(int id);

}
