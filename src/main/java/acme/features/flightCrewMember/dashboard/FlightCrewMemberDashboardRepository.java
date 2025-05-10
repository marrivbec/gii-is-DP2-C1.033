
package acme.features.flightCrewMember.dashboard;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.employee.FlightCrewMember;

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
	Integer legsWithSeverityByCrewMember(int inValue, int outValue, int flightCrewMemberId);

	@Query("SELECT f FROM FlightAssignment f JOIN f.leg l WHERE f.flightCrewMember.id = :flightCrewMemberId ORDER BY l.scheduledArrival ASC")
	List<FlightAssignment> findFlightAssignment(int flightCrewMemberId);

	@Query("SELECT DISTINCT fa.flightCrewMember FROM FlightAssignment fa WHERE fa.leg.id = :legId")
	List<FlightCrewMember> findCrewMembersInLastLeg(int legId);

	@Query("SELECT f.currentStatus, COUNT(f) FROM FlightAssignment f WHERE f.flightCrewMember.id = :flightCrewMemberId GROUP BY f.currentStatus")
	List<Object[]> flightAssignmentsGroupedByStatus(int flightCrewMemberId);

	@Query("SELECT COUNT(f) FROM FlightAssignment f WHERE f.leg.scheduledArrival >= :moment AND f.flightCrewMember.id = :crewMemberId")
	Integer countFlightAssignmentsLastYear(Date moment, int crewMemberId);

	@Query("SELECT COUNT(fa) FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :crewMemberId AND EXTRACT(YEAR FROM fa.leg.scheduledArrival) = :year AND EXTRACT(MONTH FROM fa.leg.scheduledArrival) = :month")
	Integer countFlightAssignmentsPerMonthAndYear(int crewMemberId, int year, int month);

}
