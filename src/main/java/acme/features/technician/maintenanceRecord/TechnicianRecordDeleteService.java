
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.task.Involves;
import acme.features.technician.involvedIn.TechnicianInvolvesRepository;
import acme.realms.employee.Technician;

@GuiService
public class TechnicianRecordDeleteService extends AbstractGuiService<Technician, MaintenanceRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianRecordRepository		repository;

	@Autowired
	private TechnicianInvolvesRepository	involvedRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		MaintenanceRecord maintenanceRecord;
		Technician technician;

		id = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findRecordById(id);
		technician = maintenanceRecord == null ? null : maintenanceRecord.getTechnician();
		status = maintenanceRecord != null && maintenanceRecord.isDraftMode() && super.getRequest().getPrincipal().hasRealm(technician);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord record;
		int id;

		id = super.getRequest().getData("id", int.class);
		record = this.repository.findRecordById(id);

		super.getBuffer().addData(record);

	}

	@Override
	public void bind(final MaintenanceRecord record) {
		super.bindObject(record, "maintenanceMoment", "status", "nextInspectionDue", "estimatedCost", "notes");

	}

	@Override
	public void validate(final MaintenanceRecord record) {

		if (!super.getBuffer().getErrors().hasErrors("draftMode"))
			super.state(record.isDraftMode(), "draftMode", "customers.form.error.draft-mode");

	}

	@Override
	public void perform(final MaintenanceRecord record) {
		Collection<Involves> br;

		br = this.repository.findAllInvolvedInById(record.getId());
		this.involvedRepository.deleteAll(br);
		this.repository.delete(record);

	}

	@Override
	public void unbind(final MaintenanceRecord record) {
		/*
		 * Dataset dataset;
		 * SelectChoices choices;
		 * SelectChoices aircraftChoices;
		 * 
		 * Collection<Aircraft> aircrafts;
		 * aircrafts = this.repository.getAllAircraft();
		 * aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", record.getAircraft());
		 * 
		 * choices = SelectChoices.from(MaintenanceStatus.class, record.getStatus());
		 * 
		 * dataset = super.unbindObject(record, "maintenanceMoment", "status", "nextInspectionDue", "estimatedCost", "notes", "draftMode");
		 * dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		 * dataset.put("aircrafts", aircraftChoices);
		 * dataset.put("status", choices);
		 * // Derived attributes --------------------
		 * //no tengo
		 * super.getResponse().addData(dataset);
		 */
	}
}
