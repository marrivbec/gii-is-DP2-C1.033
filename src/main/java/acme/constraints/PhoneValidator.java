
package acme.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

	@Override
	public void initialize(final ValidPhone constraintAnnotation) {
	}

	@Override
	public boolean isValid(final String phone, final ConstraintValidatorContext context) {
		if (phone == null || phone.isEmpty())
			return true;

		return phone.matches("^\\+?\\d{6,15}$");
	}
}
