
package acme.features.technician.involvedIn;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.task.Involves;
import acme.entities.task.Task;

import acme.realms.employee.Technician;

@GuiService
public class TechnicianInvolvesShowService extends AbstractGuiService<Technician, Involves> {

	@Autowired
	private TechnicianInvolvesRepository repository;


	@Override
	public void authorise() {

		boolean status;
		int id;
		Technician tech = null;

		Involves involves;

		id = super.getRequest().getData("id", int.class);
		involves = this.repository.findInvolvesById(id);
		if (involves != null)
			tech = involves.getTask().getTechnician();
		status = involves != null && super.getRequest().getPrincipal().hasRealm(tech);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Involves involves;
		int id;

		id = super.getRequest().getData("id", int.class);
		involves = this.repository.findInvolvesById(id);

		super.getBuffer().addData(involves);
	}

	@Override
	public void unbind(final Involves involves) {
		Dataset dataset;
		SelectChoices taskChoices;
		Collection<Task> tasks;
		final boolean draftRecord;

		tasks = this.repository.findAllTasks();
		taskChoices = SelectChoices.from(tasks, "description", involves.getTask());

		dataset = super.unbindObject(involves, "task");
		dataset.put("maintenanceRecord", involves.getMaintenanceRecord().getId());
		dataset.put("task", taskChoices.getSelected().getKey());
		dataset.put("tasks", taskChoices);
		dataset.put("taskTechnician", involves.getTask().getTechnician().getEmployeeCode());

		draftRecord = involves.getMaintenanceRecord().isDraftMode();
		super.getResponse().addGlobal("draftRecord", draftRecord);

		super.getResponse().addData(dataset);
	}

}
