
package acme.features.customer.bookingRecord;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.BookingRecord;
import acme.realms.client.Customer;

@GuiService
public class CustomerBookingRecordShowService extends AbstractGuiService<Customer, BookingRecord> {

	@Autowired
	public CustomerBookingRecordRepository repository;


	@Override
	public void authorise() {
		int customerId = this.getRequest().getPrincipal().getActiveRealm().getId();
		Customer customer = this.repository.findCustomerById(customerId);
		int bookingRecordId = super.getRequest().getData("id", int.class);
		BookingRecord bookingRecord = this.repository.findBookingRecordById(bookingRecordId);
		super.getResponse().setAuthorised(bookingRecord != null && customer.equals(bookingRecord.getBooking().getCustomer()));
	}
	@Override
	public void load() {
		int passengerId = super.getRequest().getData("id", int.class);
		super.getBuffer().addData(this.repository.findBookingRecordById(passengerId));
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset = super.unbindObject(bookingRecord, "passenger.fullName", "passenger.email", "passenger.passportNumber", "passenger.dateOfBirth", "passenger.specialNeeds");
		dataset.put("draftMode", bookingRecord.getBooking().isDraftMode());
		super.getResponse().addData(dataset);

	}
}
