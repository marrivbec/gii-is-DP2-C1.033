
package acme.features.airlineManager.dashboard;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AirlineManagerDashboardRepository extends AbstractRepository {

	@Query("SELECT COUNT(am) FROM AirlineManager am WHERE am.yearsExp > :yearsExp")
	int countManagersWithMoreExperience(int yearsExp);

	@Query("SELECT COUNT(l) FROM Leg l WHERE l.status = 'ON_TIME' AND l.flight.airlineManager.id = :id")
	int countOnTimeLegs(int id);

	@Query("SELECT COUNT(l) FROM Leg l WHERE l.status = 'DELAYED' AND l.flight.airlineManager.id = :id")
	int countDelayedLegs(int id);

	@Query("SELECT l.departureAirport, COUNT(l) FROM Leg l WHERE l.flight.airlineManager.id =:id GROUP BY l.departureAirport ORDER BY COUNT(l) DESC")
	List<Object[]> countLegsByOrigin(int id);

	@Query("SELECT l.arrivalAirport, COUNT(l) FROM Leg l WHERE l.flight.airlineManager.id = :id GROUP BY l.arrivalAirport ORDER BY COUNT(l) DESC")
	List<Object[]> countLegsByDestination(int id);

	@Query("SELECT l.status, COUNT(l) FROM Leg l WHERE l.flight.airlineManager.id = :id GROUP BY l.status")
	List<Object[]> countLegsByStatus(int id);

	@Query("SELECT COUNT(f) FROM Flight f WHERE f.airlineManager.id = :id")
	Integer countFlights(int id);

	@Query("SELECT AVG(f.cost.amount ) FROM Flight f WHERE f.airlineManager.id = :id")
	Double getAverageFlightCost(int id);

	@Query("SELECT MAX(f.cost.amount) FROM Flight f WHERE f.airlineManager.id = :id")
	Double getMaxFlightCost(int id);

	@Query("SELECT MIN(f.cost.amount) FROM Flight f WHERE f.airlineManager.id = :id")
	Double getMinFlightCost(int id);

	@Query("SELECT STDDEV(f.cost.amount ) FROM Flight f WHERE f.airlineManager.id = :id")
	Double getSTDEVFlightCost(int id);

}
