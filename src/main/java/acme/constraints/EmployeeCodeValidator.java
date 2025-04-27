
package acme.constraints;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.basis.AbstractRealm;
import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class EmployeeCodeValidator extends AbstractValidator<ValidEmployeeCode, AbstractRealm> {

	private static final Pattern EMPLOYEECODE_PATTERN = Pattern.compile("^([A-Z]{2,3})(\\d{6})$");


	@Override
	protected void initialise(final ValidEmployeeCode annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AbstractRealm realm, final ConstraintValidatorContext context) {

		String codigo = this.getIdentifierFromRealm(realm);

		boolean result;

		DefaultUserIdentity identity = realm.getIdentity();

		if (codigo != null && !codigo.isEmpty()) {
			boolean cod = codigo.matches("^([A-Z]{2,3})(\\d{6})$");
			if (!cod)
				super.state(context, cod, "employeeCode", "acme.validation.employeeCode.message");
			else {
				String letras = codigo.substring(0, codigo.length() - 6); // Extrae las letras
				String expectedInitials;

				if (letras.length() == 2)
					expectedInitials = this.getInitials(identity.getName(), identity.getSurname(), realm);
				else if (letras.length() == 3)
					expectedInitials = this.getInitials1(identity.getName(), identity.getSurname(), realm);
				else
					expectedInitials = ""; // no válido si tiene más o menos de 2 o 3 letras

				boolean cod1 = letras.equals(expectedInitials);

				super.state(context, cod1, "employeeCode", "acme.validation.employeeCode.message");
			}
		} else
			super.state(context, false, "employeeCode", "acme.validation.employeeCode.message");
		result = !super.hasErrors(context);

		return result;
	}

	private String getInitials(final String name, final String surname, final AbstractRealm realm) {
		if (name == null || surname == null || name.isEmpty() || surname.isEmpty())
			return "";

		return name.substring(0, 1).toUpperCase() + surname.substring(0, 1).toUpperCase();
	}

	private String getInitials1(final String name, final String surname, final AbstractRealm realm) {
		if (name == null || surname == null || name.isEmpty() || surname.isEmpty())
			return "";

		String initials = name.substring(0, 1).toUpperCase() + surname.substring(0, 1).toUpperCase();

		if (surname.length() > 1)

			initials += surname.substring(1, 2).toUpperCase(); // Tercera letra opcional

		return initials;
	}

	private String getIdentifierFromRealm(final AbstractRealm realm) {
		try {
			return (String) realm.getClass().getMethod("getEmployeeCode").invoke(realm);
		} catch (Exception e) {
			return null;
		}
	}
}
