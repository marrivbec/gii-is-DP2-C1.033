
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.maintenanceRecord.MaintenanceStatus;
import acme.entities.task.Involves;
import acme.features.technician.involvedIn.TechnicianInvolvedInRepository;
import acme.realms.employee.Technician;

@GuiService
public class TechnicianRecordDeleteService extends AbstractGuiService<Technician, MaintenanceRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianRecordRepository		repository;

	@Autowired
	private TechnicianInvolvedInRepository	involvedRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean status;
		int recordId;
		MaintenanceRecord record;
		Technician tech;

		recordId = super.getRequest().getData("id", int.class);
		record = this.repository.findRecordById(recordId);
		tech = record.getTechnician() != null ? record.getTechnician() : null;
		status = record != null && record.isDraftMode() && super.getRequest().getPrincipal().hasRealm(tech);

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
		super.bindObject(record, "MaintenanceMoment", "status", "nextMaintenance", "estimatedCost", "notes");

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
		//no se yo...
		Dataset dataset;
		SelectChoices choices;
		SelectChoices aircraftChoices;

		Collection<Aircraft> aircrafts;
		aircrafts = this.repository.getAllAircraft();
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", record.getAircraft());

		choices = SelectChoices.from(MaintenanceStatus.class, record.getStatus());

		dataset = super.unbindObject(record, "MaintenanceMoment", "status", "nextMaintenance", "estimatedCost", "notes", "draftMode");
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("status", choices);
		// Derived attributes --------------------
		//no tengo
		super.getResponse().addData(dataset);
	}
}
