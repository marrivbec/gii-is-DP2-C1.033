
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.realms.client.Customer;

@GuiService
public class CustomerBookingRecordListService extends AbstractGuiService<Customer, BookingRecord> {

	@Autowired
	public CustomerBookingRecordRepository repository;


	@Override
	public void authorise() {
		int bookingId = super.getRequest().getData("bookingId", int.class);
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Customer customer = this.repository.findCustomerById(customerId);
		Booking booking = this.repository.findBookingById(bookingId);
		super.getResponse().setAuthorised(booking != null && booking.getCustomer().equals(customer));
	}
	@Override
	public void load() {
		int bookingId = super.getRequest().getData("bookingId", int.class);
		Collection<BookingRecord> passengers = this.repository.findBookingRByBId(bookingId);
		super.getBuffer().addData(passengers);
		super.getResponse().addGlobal("bookingId", bookingId);
		super.getResponse().addGlobal("draftMode", this.repository.findBookingById(bookingId).isDraftMode());
	}

	@Override
	public void unbind(final BookingRecord record) {
		Dataset dataset;
		dataset = super.unbindObject(record, "passenger.fullName", "passenger.email", "passenger.passportNumber", "passenger.dateOfBirth", "passenger.specialNeeds", "passenger.draftMode");
		super.getResponse().addData(dataset);
	}
}
