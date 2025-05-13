
package acme.features.authenticated.airlineManager;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.UserAccount;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.realms.employee.AirlineManager;

@GuiService
public class AuthenticatedAirlineManagerCreateService extends AbstractGuiService<Authenticated, AirlineManager> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedAirlineManagerRepository repository;

	// AbstractService<Authenticated, Customers> ---------------------------


	@Override
	public void authorise() {
		boolean status;
		boolean status2;
		String method;
		Airline airline;
		int airlineId;
		method = super.getRequest().getMethod();
		status = !super.getRequest().getPrincipal().hasRealmOfType(AirlineManager.class);

		if (method.equals("GET"))
			status2 = status;
		else {
			airlineId = super.getRequest().getData("airline", int.class);
			airline = this.repository.findAirlineById(airlineId);

			status2 = airlineId == 0 || airline != null;
		}
		super.getResponse().setAuthorised(status && status2);
	}

	@Override
	public void load() {
		AirlineManager object;
		int userAccountId;
		UserAccount userAccount;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		userAccount = this.repository.findUserAccountById(userAccountId);

		object = new AirlineManager();
		object.setUserAccount(userAccount);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final AirlineManager object) {

		super.bindObject(object, "employeeCode", "yearsExp", "dateBirth", "urlImage", "airline");
	}

	@Override
	public void validate(final AirlineManager object) {

		String cod = object.getEmployeeCode();
		Collection<AirlineManager> codigo = this.repository.findAirlineManagerByCode(cod);
		super.state(codigo.isEmpty(), "employeeCode", "acme.validation.error.repeat-code");
	}

	@Override
	public void perform(final AirlineManager object) {

		this.repository.save(object);
	}

	@Override
	public void unbind(final AirlineManager object) {
		Dataset dataset;
		SelectChoices choicesAirline;
		Collection<Airline> airlines;

		airlines = this.repository.findAllAirlines();
		choicesAirline = SelectChoices.from(airlines, "name", object.getAirline());

		dataset = super.unbindObject(object, "employeeCode", "yearsExp", "dateBirth", "urlImage", "airline");
		dataset.put("airlines", choicesAirline);

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
