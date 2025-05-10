
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.BookingRecord;
import acme.entities.passenger.Passenger;
import acme.realms.client.Customer;

@GuiService
public class CustomerPassengerDeleteService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		int customerId = this.getRequest().getPrincipal().getActiveRealm().getId();
		Customer customer = this.repository.findCustomerById(customerId);
		int passengerId = super.getRequest().getData("id", int.class);
		Passenger passenger = this.repository.findPassengerById(passengerId);
		super.getResponse().setAuthorised(passenger != null && passenger.isDraftMode() && passenger.getCustomer().equals(customer));
	}

	@Override
	public void load() {

		int passengerId = super.getRequest().getData("id", int.class);
		Passenger passenger = this.repository.findPassengerById(passengerId);
		super.getBuffer().addData(passenger);

	}

	@Override
	public void validate(final Passenger passenger) {
		boolean confirmation;
		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
		boolean notPublished = passenger.isDraftMode();
		super.state(notPublished, "draftMode", "acme.validation.update.draftMode");
	}

	@Override
	public void perform(final Passenger passenger) {
		Collection<BookingRecord> bookingRecords = this.repository.findBookingRByPassengerId(passenger.getId());
		bookingRecords.stream().forEach(r -> this.repository.delete(r));
		this.repository.delete(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds");
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;
		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");
		super.getResponse().addData(dataset);
	}

}
