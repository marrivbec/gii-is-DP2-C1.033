
package acme.features.authenticated.customer;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.UserAccount;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.client.Customer;

@GuiService
public class AuthenticatedCustomerCreateService extends AbstractGuiService<Authenticated, Customer> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedCustomerRepository repository;

	// AbstractService<Authenticated, Customers> ---------------------------


	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Customer object;
		int userAccountId;
		UserAccount userAccount;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		userAccount = this.repository.findUserAccountById(userAccountId);

		object = new Customer();
		object.setUserAccount(userAccount);
		object.setEmployeeCode(GenerateCode.generateValidCode(object));

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Customer object) {

		super.bindObject(object, "employeeCode", "phoneNumber", "physicalAddress", "city", "country", "earnedPoints");
	}

	@Override
	public void validate(final Customer object) {

		String cod = object.getEmployeeCode();
		Collection<Customer> codigo = this.repository.findCustomerByCode(cod);
		super.state(codigo.isEmpty(), "employeeCode", "acme.validation.error.repeat-code");
	}

	@Override
	public void perform(final Customer object) {

		this.repository.save(object);
	}

	@Override
	public void unbind(final Customer object) {
		Dataset dataset;

		dataset = super.unbindObject(object, "employeeCode", "phoneNumber", "physicalAddress", "city", "country", "earnedPoints");

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
