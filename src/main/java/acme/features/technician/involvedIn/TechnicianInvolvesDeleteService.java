
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
public class TechnicianInvolvedInDeleteService extends AbstractGuiService<Technician, Involves> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianInvolvedInRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		Technician tech;
		boolean status;
		MaintenanceRecord record;
		Task task;
		tech = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		int involvedInId = super.getRequest().getData("id", int.class);
		task = this.repository.findOneTaskByInvolvedIn(involvedInId);
		record = this.repository.findOneRecordByInvolvedIn(involvedInId);
		status = record != null && task != null && record.isDraftMode() && super.getRequest().getPrincipal().hasRealm(tech);
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
		//Mirar esta validacion...
		super.state(involved.getMaintenanceRecord().isDraftMode(), "*", "customers.form.error.draft-mode");

	}

	@Override
	public void perform(final Involves involved) {

		this.repository.delete(involved);

	}

	@Override
	public void unbind(final Involves involved) {
		Dataset dataset;
		Technician tech = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		Collection<Task> task = this.repository.findTaskByTechnicianId(tech.getId());

		Collection<MaintenanceRecord> record = this.repository.findNotPublishRecord(tech.getId(), true);

		SelectChoices taskChoices;
		SelectChoices recordChoices;

		taskChoices = SelectChoices.from(task, "description", involved.getTask());

		recordChoices = SelectChoices.from(record, "maintanenceMoment", involved.getMaintenanceRecord());

		dataset = super.unbindObject(involved, "maintanenceRecord", "task");
		dataset.put("maintanenceRecord", recordChoices);
		dataset.put("task", taskChoices);
		//IsDraftMode()
		dataset.put("draftMode", involved.getMaintenanceRecord().isDraftMode());

		super.getResponse().addData(dataset);
	}

}
