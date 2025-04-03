
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.realms.client.Customer;

@GuiService
public class CustomerBookingListService extends AbstractGuiService<Customer, Booking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Booking> booking;
		int customerId;
		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		booking = this.repository.findAllBookings(customerId);

		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		Money price = booking.price();
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastNibble", "draftMode");
		dataset.put("price", price);
		//		super.addPayload(dataset, booking, //
		//			"description", "moreInfo", "contractor.name", //
		//			"employer.identity.fullName", "employer.area", "employer.sector");

		super.getResponse().addData(dataset);
	}

}
