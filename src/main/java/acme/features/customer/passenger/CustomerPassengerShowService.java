
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.realms.client.Customer;

@GuiService
public class CustomerPassengerShowService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	public CustomerPassengerRepository passengerRepository;


	@Override
	public void authorise() {
		int customerId = this.getRequest().getPrincipal().getActiveRealm().getId();
		Customer customer = this.passengerRepository.findCustomerById(customerId);
		int passengerId = super.getRequest().getData("id", int.class);
		Passenger passenger = this.passengerRepository.findPassengerById(passengerId);
		super.getResponse().setAuthorised(passenger != null && customer.equals(passenger.getCustomer()));
	}
	@Override
	public void load() {
		int passengerId = super.getRequest().getData("id", int.class);
		super.getBuffer().addData(this.passengerRepository.findPassengerById(passengerId));
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;
		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");
		dataset.put("readonly", !passenger.isDraftMode());

		super.getResponse().addData(dataset);
	}
}
