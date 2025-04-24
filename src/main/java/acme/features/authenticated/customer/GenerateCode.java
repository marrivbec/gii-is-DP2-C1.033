
package acme.features.authenticated.customer;

import acme.client.components.basis.AbstractRole;
import acme.client.components.principals.DefaultUserIdentity;

public class GenerateCode {

	public static String generateValidCode(final AbstractRole role) {
		if (role == null || role.getIdentity() == null)
			throw new IllegalArgumentException("Role or identity must not be null");

		DefaultUserIdentity identity = role.getIdentity();

		String name = identity.getName();
		String surname = identity.getSurname();

		if (name == null || surname == null || name.isEmpty() || surname.isEmpty())
			throw new IllegalArgumentException("Name and surname must not be empty");

		String initials = name.substring(0, 1).toUpperCase() + surname.substring(0, 1).toUpperCase();

		String codePrefix = initials;
		if (surname.length() > 1)
			if (codePrefix.length() == 2)
				initials += surname.substring(1, 2).toUpperCase();

		//dejo q el cliente añade el numero
		return initials;
	}

}
