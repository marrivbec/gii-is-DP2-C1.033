
package acme.features.customer.booking;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.realms.client.Customer;

@GuiService
public class CustomerBookingShowService extends AbstractGuiService<Customer, Booking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		int customerId;
		Booking booking;
		Customer customer;
		Customer customerLogged;
		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		customerLogged = this.repository.findCustomerLogged(customerId);
		masterId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(masterId);
		customer = booking == null ? null : booking.getCustomer();
		status = super.getRequest().getPrincipal().hasRealm(customer) && booking.getCustomer().equals(customerLogged);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking booking;
		int id;
		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);
		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		Money price = booking.price();
		SelectChoices choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "lastNibble", "flight.tag", "customer.identifier");
		dataset.put("price", price);
		dataset.put("choices", choices);
		dataset.put("travelClass", choices.getSelected().getKey());
		super.getResponse().addData(dataset);
	}

}
