/*
 * AssistanceAgentDashboardRepository.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.assistanceAgent.dashboard;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.employee.AssistanceAgent;

@Repository
public interface AssistanceAgentDashboardRepository extends AbstractRepository {

	@Query("SELECT (COUNT(c) * 100.0 / (SELECT COUNT(c2) FROM Claim c2 WHERE c2.assistanceAgents = :assistanceAgent)) FROM Claim c WHERE c.assistanceAgents = :assistanceAgent AND EXISTS (SELECT 1 FROM TrackingLog t WHERE t.claim = c AND t.indicator = ACCEPTED)")
	Double resolvedClaimsRatio(AssistanceAgent assistanceAgent);

	@Query("SELECT (COUNT(c) * 100.0 / (SELECT COUNT(c2) FROM Claim c2 WHERE c2.assistanceAgents = :assistanceAgent)) FROM Claim c WHERE c.assistanceAgents = :assistanceAgent AND EXISTS (SELECT 1 FROM TrackingLog t WHERE t.claim = c AND t.indicator = REJECTED)")
	Double rejectedClaimsRatio(AssistanceAgent assistanceAgent);

	@Query("SELECT FUNCTION('DATE_FORMAT', c.registrationMoment, '%Y-%m') AS month, COUNT(c) AS claimCount FROM Claim c WHERE c.assistanceAgents = :assistanceAgent GROUP BY FUNCTION('DATE_FORMAT', c.registrationMoment, '%Y-%m') ORDER BY claimCount DESC")
	List<Object[]> topThreeMonthsWithHighestClaims(AssistanceAgent assistanceAgent);

	@Query("SELECT AVG((SELECT COUNT(t) FROM TrackingLog t WHERE t.claim = c)) FROM Claim c WHERE c.assistanceAgents = :assistanceAgents")
	Double averageNumberOfLogsPerClaim(AssistanceAgent assistanceAgent);

	@Query("SELECT MIN((SELECT COUNT(t) FROM TrackingLog t WHERE t.claim = c)) FROM Claim c WHERE c.assistanceAgents = :assistanceAgents")
	Double minimumNumberOfLogsPerClaim(AssistanceAgent assistanceAgent);

	@Query("SELECT MAX((SELECT COUNT(t) FROM TrackingLog t WHERE t.claim = c)) FROM Claim c WHERE c.assistanceAgents = :assistanceAgents")
	Double maximumNumberOfLogsPerClaim(AssistanceAgent assistanceAgent);

	@Query("SELECT STDDEV((SELECT COUNT(t) FROM TrackingLog t WHERE t.claim = c)) FROM Claim c WHERE c.assistanceAgents = :assistanceAgents")
	Double deviationNumberOfLogsPerClaim(AssistanceAgent assistanceAgent);

}
