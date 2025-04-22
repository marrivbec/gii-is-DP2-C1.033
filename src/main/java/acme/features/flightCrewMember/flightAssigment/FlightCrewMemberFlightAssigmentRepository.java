
package acme.features.flightCrewMember.flightAssigment;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignment.DutyType;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.employee.FlightCrewMember;

@Repository
public interface FlightCrewMemberFlightAssigmentRepository extends AbstractRepository {

	@Query("SELECT l FROM Leg l")
	Collection<Leg> findAllLegs();

	@Query("SELECT flightAssig FROM FlightAssignment flightAssig WHERE flightAssig.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("SELECT flightAssig FROM FlightAssignment flightAssig WHERE flightAssig.flightCrewMember.id = :flightCrewMemberId AND flightAssig.leg.scheduledDeparture >= :moment")
	Collection<FlightAssignment> findAllPlannedFlightAssignments(Date moment, int flightCrewMemberId);

	@Query("SELECT flightAssig FROM FlightAssignment flightAssig WHERE flightAssig.flightCrewMember.id = :flightCrewMemberId AND flightAssig.leg.scheduledArrival < :moment")
	Collection<FlightAssignment> findAllCompletedFlightAssignments(Date moment, int flightCrewMemberId);

	@Query("SELECT flightCrewMem FROM FlightCrewMember flightCrewMem WHERE flightCrewMem.airline.id = :airlineId")
	Collection<FlightCrewMember> findAllflightCrewMemberFromAirline(int airlineId);

	@Query("SELECT flightAssig FROM FlightAssignment flightAssig WHERE flightAssig.leg.id = :legId AND flightAssig.duty = :duty")
	FlightAssignment findFlightAssignmentByLegAndDuty(int legId, DutyType duty);

	@Query("SELECT COUNT(flightAssig) > 0 FROM FlightAssignment flightAssig WHERE flightAssig.leg.id = :legId AND flightAssig.duty IN ('PILOT', 'COPILOT') AND flightAssig.duty = :duty AND flightAssig.id != :id")
	Boolean hasDutyAssigned(int legId, DutyType duty, int id);

	@Query("SELECT COUNT(flightAssig) > 0 FROM FlightAssignment flightAssig WHERE flightAssig.flightCrewMember.id = :flightCrewMemberId AND flightAssig.moment = :moment")
	Boolean hasFlightCrewMemberLegAssociated(int flightCrewMemberId, Date moment);

	@Query("SELECT l FROM Leg l WHERE l.aircraft.airline.id = :airlineId")
	Collection<Leg> findAllLegsFromAirline(int airlineId);

}
