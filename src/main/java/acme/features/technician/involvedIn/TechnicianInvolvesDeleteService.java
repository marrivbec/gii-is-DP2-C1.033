
package acme.features.technician.involvedIn;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.task.Involves;
import acme.realms.employee.Technician;

@GuiService
public class TechnicianInvolvesDeleteService extends AbstractGuiService<Technician, Involves> {

	@Autowired
	private TechnicianInvolvesRepository repository;


	@Override
	public void authorise() {
		boolean status = false;
		int id;
		Involves mt;
		int technicianId;

		if (!super.getRequest().getMethod().equals("GET")) {
			id = super.getRequest().getData("id", int.class);
			mt = this.repository.findInvolvesById(id);
			if (mt != null) {
				MaintenanceRecord mr = mt.getMaintenanceRecord();
				if (mr.isDraftMode()) {
					technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
					status = technicianId == mr.getTechnician().getId();
				}
			}
		}

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
	public void bind(final Involves involves) {

	}

	@Override
	public void validate(final Involves involves) {
		;
	}

	@Override
	public void perform(final Involves involves) {
		this.repository.delete(involves);
	}

	@Override
	public void unbind(final Involves involves) {
		//
	}
}
