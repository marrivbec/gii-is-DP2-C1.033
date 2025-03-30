
package acme.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LongTextValidator implements ConstraintValidator<ValidLongText, String> {

	private static final int	MIN_LENGTH	= 1;
	private static final int	MAX_LENGTH	= 255;


	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		if (value == null || value.isEmpty())
			return true;
		int length = value.trim().length();
		return length >= LongTextValidator.MIN_LENGTH && length <= LongTextValidator.MAX_LENGTH;
	}
}
