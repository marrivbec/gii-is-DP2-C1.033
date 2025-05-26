
package acme.features.customer.bookingRecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.passenger.Passenger;
import acme.realms.client.Customer;

@GuiService
public class CustomerBookingRecordCreateService extends AbstractGuiService<Customer, BookingRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int customerId = this.getRequest().getPrincipal().getActiveRealm().getId();
		int bookingId = super.getRequest().getData("bookingId", int.class);
		Customer customer = this.repository.findCustomerById(customerId);
		Booking booking = this.repository.findBookingById(bookingId);
		boolean validPassenger = true;
		boolean validId = true;
		if (super.getRequest().getMethod().equals("POST")) {

			int passengerId = super.getRequest().getData("passenger", int.class);
			if (passengerId != 0) {
				Passenger passenger = this.repository.findPassengerById(passengerId);
				validPassenger = passenger != null && passenger.getCustomer().equals(customer) && this.repository.findBookingRByBId(bookingId).stream().noneMatch(r -> r.getPassenger().equals(passenger));
			}
			int id = super.getRequest().getData("id", int.class, 0);
			validId = id == 0;
		}
		super.getResponse().setAuthorised(booking != null && booking.getCustomer().equals(customer) && validPassenger && validId);
	}

	@Override
	public void load() {
		int bookingId = super.getRequest().getData("bookingId", int.class);
		BookingRecord bookingRecord = new BookingRecord();
		Booking booking = this.repository.findBookingById(bookingId);
		bookingRecord.setBooking(booking);
		super.getBuffer().addData(bookingRecord);
	}

	@Override
	public void bind(final BookingRecord bookingRecord) {
		super.bindObject(bookingRecord, "passenger");
	}

	@Override
	public void validate(final BookingRecord bookingRecord) {
		;
	}

	@Override
	public void perform(final BookingRecord bookingRecord) {
		this.repository.save(bookingRecord);

	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {

		int customerId = this.getRequest().getPrincipal().getActiveRealm().getId();
		int bookingId = super.getRequest().getData("bookingId", int.class);
		List<Passenger> passengers = this.repository.findPassengersForBooking(customerId, bookingId);
		SelectChoices choices = SelectChoices.from(passengers, "fullName", bookingRecord.getPassenger());
		Dataset dataset = super.unbindObject(bookingRecord, "booking", "passenger");
		dataset.put("choices", choices);
		dataset.put("draftMode", bookingRecord.getBooking().isDraftMode());
		dataset.put("bookingId", bookingRecord.getBooking().getId());
		super.getResponse().addData(dataset);
	}

}
