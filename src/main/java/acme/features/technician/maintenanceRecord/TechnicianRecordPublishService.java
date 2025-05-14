
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
import acme.entities.task.Task;
import acme.realms.employee.Technician;

@GuiService
public class TechnicianRecordPublishService extends AbstractGuiService<Technician, MaintenanceRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int mrId;
		MaintenanceRecord mr;
		Technician technician;

		mrId = super.getRequest().getData("id", int.class);
		mr = this.repository.findRecordById(mrId);

		technician = mr == null ? null : mr.getTechnician();
		status = mr != null && mr.isDraftMode() && this.getRequest().getPrincipal().hasRealm(technician);

		if (super.getRequest().hasData("aircraft")) {
			int aircraftId = super.getRequest().getData("aircraft", int.class);
			Aircraft aircraft = this.repository.findAircraftById(aircraftId);
			Collection<Aircraft> available = this.repository.getAllAircraft();

			if (aircraft == null && aircraftId != 0)
				status = false;
			else if (aircraft != null && !available.contains(aircraft))
				status = false;
		}

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
		//debe tener al menos una task publicada

		Collection<Task> tasks = this.repository.findTasksByRecordId(record.getId());

		boolean allTasksPublished = tasks.isEmpty() || tasks.stream().allMatch(task -> !task.isDraftMode());
		boolean atLeastOnePublished = tasks.stream().anyMatch(task -> !task.isDraftMode());

		super.state(allTasksPublished, "*", "technician.maintanence-record.error.unpublishedTask.message");
		super.state(atLeastOnePublished, "*", "technician.maintanence-record.error.noPublishedTasks.message");
		if (record.getAircraft() == null)
			super.state(false, "aircraft", "technician.maintanence-record.error.no-aircraft");

	}
	@Override
	public void perform(final MaintenanceRecord record) {
		record.setDraftMode(false);
		this.repository.save(record);
	}

	@Override
	public void unbind(final MaintenanceRecord record) {

		Dataset dataset;
		SelectChoices choices;
		SelectChoices aircraftChoices;
		Collection<Aircraft> aircrafts;
		aircrafts = this.repository.getAllAircraft();
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", record.getAircraft());
		choices = SelectChoices.from(MaintenanceStatus.class, record.getStatus());
		dataset = super.unbindObject(record, "maintenanceMoment", "status", "nextInspectionDue", "estimatedCost", "notes", "draftMode");
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("status", choices);
		super.getResponse().addData(dataset);
	}
}
