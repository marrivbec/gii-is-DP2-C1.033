
package acme.features.airlineManager.legs;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.entities.leg.Status;

@Repository
public interface AirlineManagerLegRepository extends AbstractRepository {

	@Query("select f from Flight f where f.id = :id")
	Flight findFlightById(int id);

	@Query("select l.flight from Leg l where l.id = :id")
	Flight findFlightByLegid(int id);

	@Query("select l from Leg l where l.id = :id")
	Leg findLegById(int id);

	@Query("select l from Leg l where l.flight.id = :masterId")
	Collection<Leg> findLegsByMasterId(int masterId);

	@Query("select f from FlightAssignment f where f.leg.id=:id")
	Collection<FlightAssignment> findAllFlightAssignmentByLegId(int id);

	@Query("select a from Aircraft a where a.airline.id = :id")
	Collection<Aircraft> findAircraftsByAirlineId(int id);

	@Query("select a from Airport a")
	Collection<Airport> findAllAirport();

	@Query("select count(l.aircraft) from Leg l where l.aircraft.id = :aircraftId and l.draftMode = false and l.status != :status and ((l.scheduledArrival >= :scheduledDeparture and l.scheduledDeparture <= :scheduledArrival) or (l.scheduledArrival >= :scheduledDeparture and l.scheduledArrival <= :scheduledArrival))")
	Integer findNumberOfLegsSolapedAircraft(Date scheduledDeparture, Date scheduledArrival, Status status, Integer aircraftId);

	@Query("select l.departureAirport from Leg l where l.flight.id = :id")
	Collection<Airport> findDepartureAircraftsByFlightId(int id);

	@Query("select l.arrivalAirport from Leg l where l.flight.id = :id")
	Collection<Airport> findArrivalAircraftsByFlightId(int id);

	@Query("select l from Leg l where l.flightNumber = :flightNumber")
	Leg findLegByFlightNumber(String flightNumber);

	@Query("select a from Aircraft a where a.id = :id")
	Aircraft findAircraftById(int id);

	@Query("select a from Airport a where a.id = :id")
	Airport findAirportById(int id);

}
