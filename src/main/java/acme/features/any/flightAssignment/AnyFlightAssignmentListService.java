
package acme.features.any.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;

@GuiService
public class AnyFlightAssignmentListService extends AbstractGuiService<Any, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyFlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<FlightAssignment> publishedFlightAssignments = this.repository.findAllPublishedFA();

		super.getBuffer().addData(publishedFlightAssignments);
	}

	@Override
	public void unbind(final FlightAssignment publishedFlightAssignments) {
		Dataset dataset = super.unbindObject(publishedFlightAssignments, "duty", "moment", "currentStatus", "remarks", "draftMode", "leg");

		dataset.put("leg", publishedFlightAssignments.getLeg().getFlightNumber());

		super.addPayload(dataset, publishedFlightAssignments, "duty", "moment", "currentStatus", "remarks", "draftMode", "leg");
		super.getResponse().addData(dataset);

	}

}
