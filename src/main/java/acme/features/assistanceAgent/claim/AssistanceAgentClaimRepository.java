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

package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.leg.Leg;
import acme.entities.trackingLog.TrackingLog;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("SELECT DISTINCT c FROM Claim c JOIN TrackingLog t ON t.claim.id = c.id WHERE t.lastUpdateMoment = (SELECT MAX(t2.lastUpdateMoment) FROM TrackingLog t2 WHERE t2.claim = c) AND (t.indicator != 'PENDING' AND c.assistanceAgents.id = :agentId)")
	Collection<Claim> findAllCompletedClaimsByAgentId(int agentId);

	@Query("SELECT c FROM Claim c WHERE c.id = :id")
	Claim findClaimById(int id);

	@Query("SELECT DISTINCT c FROM Claim c JOIN TrackingLog t ON t.claim.id = c.id WHERE t.lastUpdateMoment = (SELECT MAX(t2.lastUpdateMoment) FROM TrackingLog t2 WHERE t2.claim = c) AND (t.indicator = 'PENDING' AND c.assistanceAgents.id = :agentId)")
	Collection<Claim> findAllPendingClaimsByAgentId(int agentId);

	//"SELECT l FROM Leg l WHERE l.scheduledArrival < CURRENT_TIMESTAMP AND l.draftMode = false"
	@Query("SELECT l FROM Leg l WHERE l.scheduledArrival < CURRENT_TIMESTAMP")
	Collection<Leg> findAllLeg();

	@Query("select l from Leg l where l.id = :id")
	Leg findLegById(int id);

	@Query("select tl FROM TrackingLog tl where tl.claim.id = :claimId")
	Collection<TrackingLog> findTrackingLogsByClaimId(int claimId);
}
