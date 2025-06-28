
package acme.features.authenticated.flightCrewMember;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.realms.employee.AvailabilityStatus;
import acme.realms.employee.FlightCrewMember;

@GuiService
public class AuthenticatedFlightCrewMemberUpdateService extends AbstractGuiService<Authenticated, FlightCrewMember> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedFlightCrewMemberRepository repository;

	// AbstractService<Authenticated, Customers> ---------------------------


	@Override
	public void authorise() {
		boolean status;
		boolean status2;
		String method;
		Airline airline;
		int airlineId;

		status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);

		method = super.getRequest().getMethod();

		if (method.equals("GET"))
			status2 = status;
		else {
			airlineId = super.getRequest().getData("airline", int.class);
			airline = this.repository.findAirlineById(airlineId);
			status2 = (airlineId == 0 || airline != null) && status;
		}

		super.getResponse().setAuthorised(status2);
	}

	@Override
	public void load() {
		FlightCrewMember object;
		int userAccountId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		object = this.repository.findFlightCrewMemberByUserAccountId(userAccountId);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final FlightCrewMember object) {

		super.bindObject(object, "employeeCode", "phoneNumber", "languageSkills", "availabilityStatus", "salary", "yearsOfExperience", "airline");
	}

	@Override
	public void validate(final FlightCrewMember object) {

		String cod = object.getEmployeeCode();
		Collection<FlightCrewMember> codigo = this.repository.findFligthCrewMemberByCode(cod).stream().filter(x -> x.getId() != object.getId()).toList();
		super.state(codigo.isEmpty(), "employeeCode", "acme.validation.error.repeat-code");
	}

	@Override
	public void perform(final FlightCrewMember object) {

		this.repository.save(object);
	}

	@Override
	public void unbind(final FlightCrewMember object) {
		Dataset dataset;

		dataset = super.unbindObject(object, "employeeCode", "phoneNumber", "languageSkills", "availabilityStatus", "salary", "yearsOfExperience", "airline");

		SelectChoices statusChoices = SelectChoices.from(AvailabilityStatus.class, object.getAvailabilityStatus());
		dataset.put("statusChoices", statusChoices);
		dataset.put("availabilityStatus", statusChoices.getSelected().getKey());

		SelectChoices airlineChoices = SelectChoices.from(this.repository.findAllAirlines(), "name", object.getAirline());
		dataset.put("airlineChoices", airlineChoices);
		dataset.put("airline", airlineChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
