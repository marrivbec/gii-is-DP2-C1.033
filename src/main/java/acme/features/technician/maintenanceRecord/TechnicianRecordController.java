
package acme.features.technician.maintenanceRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.realms.employee.Technician;

@GuiController
public class TechnicianRecordController extends AbstractGuiController<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianRecordServiceShow		showService;

	@Autowired
	private TechnicianRecordServiceList		listService;

	@Autowired
	private TechnicianRecordCreateService	createService;

	@Autowired
	private TechnicianRecordUpdateService	updateService;

	@Autowired
	private TechnicianRecordPublishService	publishService;

	@Autowired
	private TechnicianRecordDeleteService	deleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
