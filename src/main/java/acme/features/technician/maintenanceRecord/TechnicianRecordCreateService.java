
package acme.features.technician.maintenanceRecord;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.maintenanceRecord.MaintenanceStatus;
import acme.realms.employee.Technician;

@GuiService
public class TechnicianRecordCreateService extends AbstractGuiService<Technician, MaintenanceRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean status = true;

		if (super.getRequest().hasData("id") && super.getRequest().getData("aircraft", int.class) != 0) {
			int aircraftId = super.getRequest().getData("aircraft", int.class);
			Aircraft a = this.repository.findAircraftById(aircraftId);
			status = a != null;
			@SuppressWarnings("unused")
			MaintenanceStatus maintenanceRecordStatus = super.getRequest().getData("status", MaintenanceStatus.class);
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Technician tech;
		MaintenanceRecord record;

		tech = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		record = new MaintenanceRecord();

		record.setTechnician(tech);

		super.getBuffer().addData(record);

	}

	@Override
	public void bind(final MaintenanceRecord record) {
		int aircraftId = super.getRequest().getData("aircraft", int.class);

		Aircraft aircraft = this.repository.findAircraftById(aircraftId);
		super.bindObject(record, "maintenanceMoment", "status", "nextInspectionDue", "estimatedCost", "notes");

		record.setAircraft(aircraft);
		record.setDraftMode(true);

	}

	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {
		boolean status = true;

		Date inspection = maintenanceRecord.getNextInspectionDue();

		Date moment = maintenanceRecord.getMaintenanceMoment();

		if (inspection != null && moment != null)
			status = inspection.after(moment);

		super.state(status, "inspectionDueDate", "acme.validation.maintenanceRecord.nextInspectionPriorMaintenanceMoment.message");
	}
	@Override
	public void perform(final MaintenanceRecord record) {
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
		dataset = super.unbindObject(record, "maintenanceMoment", "status", "nextInspectionDue", "estimatedCost", "notes");
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("status", choices);

		super.getResponse().addData(dataset);

	}
}
