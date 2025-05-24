
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClass;
import acme.entities.flight.Flight;
import acme.realms.client.Customer;

@GuiService
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean validFlight = true;
		boolean validId = true;
		if (super.getRequest().getMethod().equals("POST")) {
			int flightId = super.getRequest().getData("flight", int.class);
			if (flightId != 0) {
				Flight flight = this.repository.findFlightById(flightId);
				validFlight = flight != null && !flight.isDraftMode();
			}
			int id = super.getRequest().getData("id", int.class, 0);
			validId = id == 0;

		}
		super.getResponse().setAuthorised(validFlight && validId);
	}

	@Override
	public void load() {
		Booking booking = new Booking();
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Customer customer = this.repository.findCustomerLogged(customerId);
		booking.setCustomer(customer);
		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		SelectChoices choicesFlight = SelectChoices.from(this.repository.allFlight(), "tag", booking.getFlight());
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "lastNibble");
		dataset.put("choices", choices);
		dataset.put("choicesFlight", choicesFlight);
		dataset.put("travelClass", choices.getSelected().getKey());
		dataset.put("flight", choicesFlight.getSelected().getKey());
		dataset.put("bookingId", booking.getId());
		super.getResponse().addData(dataset);
	}
	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "locatorCode", "purchaseMoment", "lastNibble", "flight", "travelClass");
	}
	@Override
	public void perform(final Booking booking) {
		booking.setDraftMode(true);
		this.repository.save(booking);
	}
	@Override
	public void validate(final Booking booking) {
		boolean confirmation;
		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
		String cod = booking.getLocatorCode();
		Collection<Booking> codigo = this.repository.findAllBookingLocatorCode(cod);
		if (!codigo.isEmpty())
			super.state(false, "locatorCode", "acme.validation.booking.repeat-code.message");
		if (booking.getFlight() != null && (booking.getPurchaseMoment() == null || booking.getFlight() == null || booking.getFlight().getScheduledDeparture() == null || !booking.getFlight().getScheduledDeparture().after(booking.getPurchaseMoment())))
			super.state(false, "purchaseMoment", "acme.validation.booking.purchaseMoment.message");

	}

}
