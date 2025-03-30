
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;

@Repository
public interface FlightCrewMemberActivityLogRepository extends AbstractRepository {

	@Query("SELECT a FROM ActivityLog a WHERE a.flightCrewMember.id = :flightCrewMemberId")
	Collection<ActivityLog> findAllActivityLogs(int flightCrewMemberId);

	@Query("SELECT a FROM ActivityLog a WHERE a.id = :activityLogId")
	ActivityLog findActivityLogById(int activityLogId);
}
