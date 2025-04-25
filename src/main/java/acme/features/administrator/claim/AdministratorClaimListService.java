/*
 * AdministratorTrackingLogListService.java
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

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;

@GuiService
public class AdministratorClaimListService extends AbstractGuiService<Administrator, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorClaimRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Claim> anouncements;

		anouncements = this.repository.findAllAnnouncementsPublished();

		super.getBuffer().addData(anouncements);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		String indicator;

		indicator = claim.indicator();
		dataset = super.unbindObject(claim, "passengerEmail", "type", "draftMode");
		dataset.put("indicator", indicator);
		super.addPayload(dataset, claim, "registrationMoment", "description", "leg");

		super.getResponse().addData(dataset);
	}

}
