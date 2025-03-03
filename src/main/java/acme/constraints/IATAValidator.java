
package acme.constraints;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IATAValidator implements ConstraintValidator<ValidIATA, String> {

	private static final Pattern IATA_PATTERN = Pattern.compile("^[A-Z]{3}$");


	@Override
	public boolean isValid(final String iata, final ConstraintValidatorContext context) {
		if (iata == null)
			return false;
		return IATAValidator.IATA_PATTERN.matcher(iata).matches();
	}
}
