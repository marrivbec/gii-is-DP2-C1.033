
package acme.constraints;

import java.time.Year;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PromoCodeValidator implements ConstraintValidator<ValidPromoCode, String> {

	private static final Pattern PROMO_PATTERN = Pattern.compile("^[A-Z]{4}-[0-9]{2}$");


	@Override
	public boolean isValid(final String promoCode, final ConstraintValidatorContext context) {
		if (promoCode == null)
			return false;

		if (!PromoCodeValidator.PROMO_PATTERN.matcher(promoCode).matches())
			return false;

		String yearSuffix = promoCode.substring(5);
		int currentYear = Year.now().getValue() % 100;

		return yearSuffix.equals(String.format("%02d", currentYear));
	}
}
