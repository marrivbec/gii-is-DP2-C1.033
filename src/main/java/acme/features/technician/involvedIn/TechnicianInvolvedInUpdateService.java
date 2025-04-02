
package acme.features.technician.involvedIn;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.task.Involves;
import acme.entities.task.Task;
import acme.realms.employee.Technician;

@GuiService
public class TechnicianInvolvedInUpdateService extends AbstractGuiService<Technician, Involves> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianInvolvedInRepository repository;


	// AbstractService<Manager, ProjectUserStoryLink> ---------------------------
	@Override
	public void authorise() {
		Technician tech;
		boolean status;
		MaintenanceRecord record;
		Task task;

		tech = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		int involvedInId = super.getRequest().getData("id", int.class);
		record = this.repository.findOneRecordByInvolvedIn(involvedInId);
		task = this.repository.findOneTaskByInvolvedIn(involvedInId);
		status = record != null && task != null && task.isDraftMode() && super.getRequest().getPrincipal().hasRealm(tech);
		//no se como mierda funciona lo del draftMode
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Involves involved;
		int id;
		id = super.getRequest().getData("id", int.class);
		involved = this.repository.findInvolvedIn(id);

		super.getBuffer().addData(involved);
	}

	@Override
	public void bind(final Involves involved) {

		super.bindObject(involved, "maintanenceRecord", "task");
	}

	@Override
	public void validate(final Involves involved) {
		MaintenanceRecord record = involved.getMaintenanceRecord();
		Task task = involved.getTask();

		super.state(record != null, "*", "technician.involved-in.create.error.null-record");
		super.state(task != null, "*", "technician.involved-in.create.error.null-task");
		//boolean exists1 = this.repository.existsByRecordAndTask(record, task);
		//super.state(!exists1, "*", "technician.involved-in.create.error.duplicate-record-task");
		;
	}

	@Override
	public void perform(final Involves involved) {
		this.repository.save(involved);
	}

	@Override
	public void unbind(final Involves involved) {
		Dataset dataset;
		Technician tech = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		Collection<MaintenanceRecord> records = this.repository.findRecordByTechnicianId(tech.getId());
		Collection<Task> tasks = this.repository.findTaskByTechnicianId(tech.getId());

		SelectChoices recordChoices;
		SelectChoices taskChoices;

		recordChoices = SelectChoices.from(records, "maintanenceMoment", involved.getMaintenanceRecord());
		taskChoices = SelectChoices.from(tasks, "description", involved.getTask());

		dataset = super.unbindObject(involved, "maintanenceRecord", "task");
		dataset.put("maintanenceRecord", recordChoices.getSelected().getKey());
		dataset.put("records", recordChoices);
		dataset.put("task", taskChoices.getSelected().getKey());
		dataset.put("tasks", taskChoices);
		MaintenanceRecord record = involved.getMaintenanceRecord();
		dataset.put("draftMode", record != null ? record.isDraftMode() : false);

		super.getResponse().addData(dataset);

	}
}
