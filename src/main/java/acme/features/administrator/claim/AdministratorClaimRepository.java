/*
 * AdministratorTrackingLogRepository.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.administrator.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.leg.Leg;

@Repository
public interface AdministratorClaimRepository extends AbstractRepository {

	@Query("SELECT c FROM Claim c WHERE c.draftMode = false")
	Collection<Claim> findAllAnnouncementsPublished();

	@Query("SELECT c FROM Claim c WHERE c.id = :id")
	Claim findClaimById(int id);

	@Query("SELECT l FROM Leg l WHERE l.scheduledArrival < CURRENT_TIMESTAMP AND l.draftMode = false")
	Collection<Leg> findAllLeg();
}
