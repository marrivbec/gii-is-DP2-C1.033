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

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.realms.employee.AssistanceAgent;

@Repository
public interface AssistanceAgentDashboardRepository extends AbstractRepository {

	@Query("SELECT c FROM Claim c WHERE c.assistanceAgents = :assistanceAgent AND c.id IN (" + "SELECT t.claim.id FROM TrackingLog t " + "WHERE t.lastUpdateMoment = (" + "SELECT MAX(t2.lastUpdateMoment) FROM TrackingLog t2 WHERE t2.claim = t.claim" + ") "
		+ "AND t.indicator = acme.entities.trackingLog.Indicator.ACCEPTED " + "AND t.resolutionPercentage = 100 " + "AND t.resolution IS NOT NULL)")
	List<Claim> countResolvedClaims(AssistanceAgent assistanceAgent);

	@Query("SELECT c FROM Claim c WHERE c.assistanceAgents = :assistanceAgent AND c.id IN (" + "SELECT t.claim.id FROM TrackingLog t " + "WHERE t.lastUpdateMoment = (" + "SELECT MAX(t2.lastUpdateMoment) FROM TrackingLog t2 WHERE t2.claim = t.claim" + ") "
		+ "AND t.indicator = acme.entities.trackingLog.Indicator.REJECTED " + "AND t.resolutionPercentage = 100 " + "AND t.resolution IS NOT NULL)")
	List<Claim> countRejectedClaims(AssistanceAgent assistanceAgent);

	@Query("SELECT COUNT(c) FROM Claim c WHERE c.assistanceAgents = :assistanceAgent")
	int countTotalClaims(AssistanceAgent assistanceAgent);

	default Double resolvedClaimsRatio(final AssistanceAgent assistanceAgent) {
		double total = this.countTotalClaims(assistanceAgent);
		double res = this.countResolvedClaims(assistanceAgent).size();
		double resolutado = 100.0 * (res / total);
		return total > 0 ? resolutado : 0.0;
	}

	default Double rejectedClaimsRatio(final AssistanceAgent assistanceAgent) {
		double total = this.countTotalClaims(assistanceAgent);
		double res = this.countRejectedClaims(assistanceAgent).size();
		double resolutado = 100.0 * (res / total);
		return total > 0 ? resolutado : 0.0;
	}

	@Query("SELECT FUNCTION('MONTHNAME', c.registrationMoment) as month, COUNT(c) as count " + "FROM Claim c WHERE c.assistanceAgents = :assistanceAgent " + "GROUP BY FUNCTION('MONTH', c.registrationMoment), month " + "ORDER BY COUNT(c) DESC")
	List<Object[]> findMonthsWithMostClaims(AssistanceAgent assistanceAgent);

	default List<String> topThreeMonthsWithHighestClaims(final AssistanceAgent assistanceAgent) {
		return this.findMonthsWithMostClaims(assistanceAgent).stream().limit(3).map(arr -> (String) arr[0]).toList();
	}

	@Query("SELECT AVG((SELECT COUNT(t) FROM TrackingLog t WHERE t.claim = c)) FROM Claim c WHERE c.assistanceAgents = :assistanceAgent")
	Double averageNumberOfLogsPerClaim(AssistanceAgent assistanceAgent);

	@Query("SELECT MIN((SELECT COUNT(t) FROM TrackingLog t WHERE t.claim = c)) FROM Claim c WHERE c.assistanceAgents = :assistanceAgent")
	Double minimumNumberOfLogsPerClaim(AssistanceAgent assistanceAgent);

	@Query("SELECT MAX((SELECT COUNT(t) FROM TrackingLog t WHERE t.claim = c)) FROM Claim c WHERE c.assistanceAgents = :assistanceAgent")
	Double maximumNumberOfLogsPerClaim(AssistanceAgent assistanceAgent);

	@Query("SELECT STDDEV((SELECT COUNT(t) FROM TrackingLog t WHERE t.claim = c)) FROM Claim c WHERE c.assistanceAgents = :assistanceAgent")
	Double deviationNumberOfLogsPerClaim(AssistanceAgent assistanceAgent);

	//
	@Query("SELECT COUNT(c) FROM Claim c WHERE c.assistanceAgents = :assistanceAgent AND YEAR(c.registrationMoment) = :year AND MONTH(c.registrationMoment) = :month")
	Integer countClaimsByMonth(AssistanceAgent assistanceAgent, int year, int month);

	//
	@Query("SELECT YEAR(c.registrationMoment) as year, MONTH(c.registrationMoment) as month, COUNT(c) as count " + "FROM Claim c WHERE c.assistanceAgents = :assistanceAgent AND c.registrationMoment BETWEEN :startDate AND :endDate "
		+ "GROUP BY YEAR(c.registrationMoment), MONTH(c.registrationMoment)")
	List<Object[]> getMonthlyClaimsCounts(AssistanceAgent assistanceAgent, Date startDate, Date endDate);

}
