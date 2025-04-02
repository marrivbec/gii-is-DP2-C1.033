
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
import acme.realms.employee.Technician;

@GuiService
public class TechnicianRecordCreateService extends AbstractGuiService<Technician, MaintenanceRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		Technician tech;

		tech = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		status = super.getRequest().getPrincipal().hasRealm(tech);
		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		//no tengo ninguna derivada mas
		Technician tech;
		MaintenanceRecord record;

		tech = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		record = new MaintenanceRecord();

		record.setTechnician(tech);

		super.getBuffer().addData(record);

	}

	@Override
	public void bind(final MaintenanceRecord record) {
		//necesito el aircraft
		int aircraftId = super.getRequest().getData("aircraft", int.class);

		Aircraft aircraft = this.repository.findAircraftById(aircraftId);
		super.bindObject(record, "maintanenceMoment", "status", "nextMaintanence", "estimatedCost", "notes");

		record.setAircraft(aircraft);
		record.setDraftMode(true);

	}

	@Override
	public void validate(final MaintenanceRecord record) {
		//aqui seran todos los errores que tienen que saltar en la pantalla 
		//para que no se cague encima...
		if (record.getAircraft() == null)
			super.state(false, "aircraft", "technician.maintanence-record.error.no-aircraft");
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
		dataset = super.unbindObject(record, "maintanenceMoment", "status", "nextMaintanence", "estimatedCost", "notes");
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("status", choices);

		super.getResponse().addData(dataset);

	}
}
