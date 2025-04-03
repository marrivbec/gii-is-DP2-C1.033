
package acme.features.technician.involvedIn;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.task.Involves;
import acme.entities.task.Task;
import acme.realms.employee.Technician;

@GuiService
public class TechnicianInvolvedInServiceList extends AbstractGuiService<Technician, Involves> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianInvolvedInRepository repository;


	// AbstractService<Manager, ProjectUserStoryLink> ---------------------------
	@Override
	public void authorise() {
		Technician tech;
		boolean status;
		tech = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		status = super.getRequest().getPrincipal().hasRealm(tech);
		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Collection<Involves> involved;
		int technicianId;

		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		involved = this.repository.findAllInvolvedInByTechnicianId(technicianId);

		super.getBuffer().addData(involved);
	}

	@Override
	public void unbind(final Involves involved) {
		Dataset dataset;
		Task task;
		MaintenanceRecord record;
		int involvedInId = involved.getId();
		record = this.repository.findOneRecordByInvolvedIn(involvedInId);

		task = this.repository.findOneTaskByInvolvedIn(involvedInId);

		dataset = super.unbindObject(involved, "maintanenceRecord", "task");
		dataset.put("maintanenceRecord", record.getMaintenanceMoment());
		dataset.put("task", task.getDescription());

		super.getResponse().addData(dataset);

	}
}
