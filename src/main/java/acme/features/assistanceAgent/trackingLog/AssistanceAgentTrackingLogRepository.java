/*
 * AssistanceAgentTrackingLogRepository.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.trackingLog.TrackingLog;

@Repository
public interface AssistanceAgentTrackingLogRepository extends AbstractRepository {

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId")
	Collection<TrackingLog> findTrackingLogsByClaimId(int claimId);

	@Query("SELECT t FROM TrackingLog t WHERE t.id = :trackingLogId")
	TrackingLog findTrackingLogById(int trackingLogId);

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.assistanceAgents.id = :assistanceAgentId")
	Collection<TrackingLog> findAllTrackingLogs(int assistanceAgentId);

	@Query("SELECT c FROM Claim c WHERE c.assistanceAgents.id = :assistanceAgentId")
	Collection<Claim> findAllClaim(int assistanceAgentId);
}
