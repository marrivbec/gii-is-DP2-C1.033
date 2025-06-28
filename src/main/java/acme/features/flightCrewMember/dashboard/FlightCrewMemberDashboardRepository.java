
package acme.features.flightCrewMember.dashboard;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flightAssignment.Status;

@Repository
public interface FlightCrewMemberDashboardRepository extends AbstractRepository {

	@Query("SELECT l.arrivalAirport.city FROM FlightAssignment f JOIN f.leg l WHERE f.flightCrewMember.id = :flightCrewMemberId ORDER BY f.moment DESC")
	List<String> findLastFiveDestinations(int flightCrewMemberId, Pageable pageable);

	@Query("""
		SELECT COUNT(DISTINCT a.flightAssignment.leg)
		FROM ActivityLog a
		WHERE a.severityLevel BETWEEN :inValue AND :outValue
		AND a.flightAssignment.flightCrewMember.id = :flightCrewMemberId
		""")
	Integer legsWithSeverity(int inValue, int outValue, int flightCrewMemberId);

	@Query("SELECT f FROM FlightAssignment f JOIN f.leg l WHERE f.flightCrewMember.id = :flightCrewMemberId ORDER BY l.scheduledArrival DESC")
	List<FlightAssignment> findFlightAssignment(int flightCrewMemberId, Pageable pageable);

	@Query("SELECT DISTINCT fa.flightCrewMember.userAccount.username FROM FlightAssignment fa WHERE fa.leg.id = :legId")
	List<String> memberLastLeg(int legId);

	@Query("SELECT COUNT(fa) FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :flightCrewMemberId AND fa.currentStatus = :status")
	int nFaByStatus(int flightCrewMemberId, Status status);

	@Query("SELECT COUNT(fa) FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :flightCrewMemberId AND EXTRACT(YEAR FROM fa.leg.scheduledArrival) = :year AND EXTRACT(MONTH FROM fa.leg.scheduledArrival) = :month")
	Integer faInMonth(int flightCrewMemberId, int year, int month);

	@Query("SELECT COUNT(fa) FROM FlightAssignment fa WHERE YEAR(fa.leg.scheduledArrival) = :moment AND fa.flightCrewMember.id = :crewMemberId")
	Integer faLastYear(int moment, int crewMemberId);

}
