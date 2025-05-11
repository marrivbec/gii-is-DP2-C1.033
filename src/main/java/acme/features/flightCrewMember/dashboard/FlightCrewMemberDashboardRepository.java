
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
		SELECT COUNT(a.flightAssignment.leg)
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

	@Query("SELECT YEAR(fa.leg.scheduledArrival) as year, MONTH(fa.leg.scheduledArrival) as month, COUNT(fa) as count " + "FROM FlightAssignment fa WHERE fa.flightCrewMember = :flightCrewMember AND fa.leg.scheduledArrival BETWEEN :startDate AND :endDate "
		+ "GROUP BY YEAR(fa.leg.scheduledArrival), MONTH(fa.leg.scheduledArrival)")
	List<Object[]> countFlightAssignmentsPerMonth(FlightCrewMember flightCrewMember, Date startDate, Date endDate);

}
