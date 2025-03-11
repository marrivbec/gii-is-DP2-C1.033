
package acme.constraints;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class PhoneValidator extends AbstractValidator<ValidPhone, String> {

	@Override
	protected void initialise(final ValidPhone annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final String phone, final ConstraintValidatorContext context) {
		// HINT: phone can be null
		assert context != null;

		boolean result;

		if (phone != null) {
			boolean inRange;

			inRange = Pattern.matches("^\\+?\\d{6,15}$", phone);
			super.state(context, inRange, "number", "acme.validation.phone.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
