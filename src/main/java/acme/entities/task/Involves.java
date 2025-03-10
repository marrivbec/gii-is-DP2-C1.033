
package acme.entities.task;

import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.entities.maintenanceRecord.MaintenanceRecord;

public class Involves extends AbstractEntity {
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Task				task;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private MaintenanceRecord	maintenanceRecord;

}
