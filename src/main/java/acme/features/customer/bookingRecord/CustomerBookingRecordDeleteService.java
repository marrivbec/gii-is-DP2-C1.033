
package acme.features.customer.bookingRecord;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.BookingRecord;
import acme.realms.client.Customer;

@GuiService
public class CustomerBookingRecordDeleteService extends AbstractGuiService<Customer, BookingRecord> {

	@Autowired
	public CustomerBookingRecordRepository repository;


	@Override
	public void authorise() {
		int customerId = this.getRequest().getPrincipal().getActiveRealm().getId();
		Customer customer = this.repository.findCustomerById(customerId);
		int id = super.getRequest().getData("id", int.class);
		BookingRecord bookingRecord = this.repository.findBookingRecordById(id);
		super.getResponse().setAuthorised(bookingRecord != null && customer.equals(bookingRecord.getBooking().getCustomer()) && bookingRecord.getBooking().isDraftMode());
	}
	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		super.getBuffer().addData(this.repository.findBookingRecordById(id));
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset = super.unbindObject(bookingRecord, "passenger.fullName", "passenger.email", "passenger.passportNumber", "passenger.dateOfBirth", "passenger.specialNeeds");
		dataset.put("draftMode", bookingRecord.getBooking().isDraftMode());
		super.getResponse().addData(dataset);
	}
	@Override
	public void perform(final BookingRecord r) {
		this.repository.delete(r);
	}
	@Override
	public void bind(final BookingRecord r) {

	}
	@Override
	public void validate(final BookingRecord r) {

	}
}
