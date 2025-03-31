
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.passenger.Passenger;
import acme.realms.client.Customer;

@GuiService
public class CustomerPassengerBookingListService extends AbstractGuiService<Customer, Passenger> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int bookingId = super.getRequest().getData("bookingId", int.class);
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Customer customer = this.repository.findCustomerById(customerId);
		Booking booking = this.repository.findBookingById(bookingId);
		super.getResponse().setAuthorised(booking.getCustomer().equals(customer));
	}

	@Override
	public void load() {
		Collection<Passenger> passenger;
		Customer customer;

		customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		int id;
		Booking booking;

		id = super.getRequest().getData("bookingId", int.class);
		booking = this.repository.findBookingById(id);

		passenger = this.repository.findPassengersByBookingId(booking.getId(), customer.getId());

		super.getBuffer().addData(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");

		super.getResponse().addData(dataset);
	}
}
