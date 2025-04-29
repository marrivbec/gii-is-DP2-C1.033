
package acme.features.administrator.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;

@GuiService
public class AdministratorBookingListService extends AbstractGuiService<Administrator, Booking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Booking> booking = this.repository.findAllPublishedBookings();

		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		Money price = booking.price();
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastNibble");
		dataset.put("price", price);

		super.getResponse().addData(dataset);
	}

}
