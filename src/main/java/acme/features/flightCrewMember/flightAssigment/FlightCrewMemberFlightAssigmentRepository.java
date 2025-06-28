
package acme.features.flightCrewMember.flightAssigment;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.DutyType;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;

@Repository
public interface FlightCrewMemberFlightAssigmentRepository extends AbstractRepository {

	@Query("SELECT l FROM Leg l WHERE l.id = :id")
	Leg findLegById(int id);

	@Query("SELECT flightAssig FROM FlightAssignment flightAssig WHERE flightAssig.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("SELECT flightAssig FROM FlightAssignment flightAssig WHERE flightAssig.flightCrewMember.id = :flightCrewMemberId AND flightAssig.leg.scheduledDeparture >= :moment")
	Collection<FlightAssignment> findAllPlannedFlightAssignments(Date moment, int flightCrewMemberId);

	@Query("SELECT flightAssig FROM FlightAssignment flightAssig WHERE flightAssig.flightCrewMember.id = :flightCrewMemberId AND flightAssig.leg.scheduledArrival < :moment")
	Collection<FlightAssignment> findAllCompletedFlightAssignments(Date moment, int flightCrewMemberId);

	@Query("SELECT COUNT(flightAssig) > 0 FROM FlightAssignment flightAssig WHERE flightAssig.leg.id = :legId AND (flightAssig.duty= acme.entities.flightAssignment.DutyType.COPILOT OR flightAssig.duty= acme.entities.flightAssignment.DutyType.PILOT) AND flightAssig.duty = :duty AND flightAssig.id != :id")
	Boolean hasDutyAssigned(int legId, DutyType duty, int id);

	@Query("""
		    SELECT COUNT(flightAssig) > 0
		    FROM FlightAssignment flightAssig
		    WHERE flightAssig.flightCrewMember.id = :flightCrewMemberId
		      AND flightAssig.id != :id
		      AND (
		           (flightAssig.leg.scheduledDeparture < :arrivalTime AND flightAssig.leg.scheduledArrival > :departureTime) OR
		           (flightAssig.leg.scheduledDeparture < :departureTime AND flightAssig.leg.scheduledArrival > :arrivalTime) OR
		           (flightAssig.leg.scheduledDeparture >= :departureTime AND flightAssig.leg.scheduledDeparture <= :arrivalTime) OR
		           (flightAssig.leg.scheduledArrival >= :departureTime AND flightAssig.leg.scheduledArrival <= :arrivalTime)
		          )
		""")
	Boolean hasFlightCrewMemberLegAssociated(int flightCrewMemberId, Date arrivalTime, Date departureTime, int id);

	@Query("SELECT l FROM Leg l WHERE l.aircraft.airline.id = :airlineId AND l.draftMode = false AND l.scheduledDeparture >= :moment")
	Collection<Leg> findAllLegsFromAirline(int airlineId, Date moment);

	@Query("select al FROM ActivityLog al where al.flightAssignment.id = :flightAssignmentId")
	Collection<ActivityLog> findActivityLogsByFlightAssignmentId(int flightAssignmentId);

}
