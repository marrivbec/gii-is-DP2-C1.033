/*
 * AdministratorAircraftUpdateService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.administrator.aircraft;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;

@GuiService
public class AdministratorAircraftUpdateService extends AbstractGuiService<Administrator, Aircraft> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAircraftRepository repository;

	// AbstractGuiService interfaced ------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Aircraft aircraft;
		int id;

		id = super.getRequest().getData("id", int.class);
		aircraft = this.repository.findAircraftById(id);

		super.getBuffer().addData(aircraft);
	}

	@Override
	public void bind(final Aircraft aircraft) {
		super.bindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details");
	}

	@Override
	public void validate(final Aircraft aircraft) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Aircraft aircraft) {
		this.repository.save(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		Dataset dataset;

		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}

}
