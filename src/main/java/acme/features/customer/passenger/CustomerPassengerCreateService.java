
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.realms.client.Customer;

@GuiService
public class CustomerPassengerCreateService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean validId = true;
		if (super.getRequest().getMethod().equals("POST")) {
			int id = super.getRequest().getData("id", int.class, 0);
			validId = id == 0;
		}
		super.getResponse().setAuthorised(validId);
	}

	@Override
	public void load() {
		Passenger passenger = new Passenger();
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		passenger.setCustomer(this.repository.findCustomerById(customerId));
		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds");
	}

	@Override
	public void validate(final Passenger passenger) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Passenger passenger) {
		passenger.setDraftMode(true);
		this.repository.save(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {

		Dataset dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds");
		dataset.put("readonly", false);
		dataset.put("confirmation", false);

		super.getResponse().addData(dataset);
	}
}
