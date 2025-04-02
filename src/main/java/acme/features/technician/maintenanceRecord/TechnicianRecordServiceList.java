
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.realms.employee.Technician;

@GuiService
public class TechnicianRecordServiceList extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianRecordRepository repository;


	// AbstractGuiService interface -------------------------------------------
	@Override
	public void authorise() {
		Technician technician;
		boolean status;
		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		status = super.getRequest().getPrincipal().hasRealm(technician);
		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Collection<MaintenanceRecord> record;
		int technicianId;

		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		record = this.repository.getMyMaintenanceRecords(technicianId);

		super.getBuffer().addData(record);
	}

	@Override
	public void unbind(final MaintenanceRecord record) {
		Dataset dataset;

		dataset = super.unbindObject(record, "status", "estimatedCost", "notes");
		super.getResponse().addData(dataset);

	}
}
