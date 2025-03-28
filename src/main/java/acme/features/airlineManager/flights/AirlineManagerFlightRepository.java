
package acme.features.airlineManager.flights;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight.Flight;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.employee.AirlineManager;

@Repository
public interface AirlineManagerFlightRepository extends AbstractRepository {

	@Query("select f from Flight f where f.id = :id")
	Flight findFlightById(int id);

	@Query("select f from Flight f")
	Collection<Flight> findAllFlights();

	@Query("select f from Flight f where f.airlineManager.id = :id")
	Collection<Flight> findAllFlightByAirlineManagerId(int id);

	@Query("select f.airlineManager from Flight f where f.airlineManager.id = :id")
	AirlineManager findAirlineManagerById(int id);

	@Query("select l from Leg l where l.flight.id=:id")
	Collection<Leg> findAllLegByFlightId(int id);

	@Query("select f from FlightAssignment f where f.leg.flight.id=:id")
	Collection<FlightAssignment> findAllFlightAssignmentByFlightId(int id);

}
