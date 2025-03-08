
package acme.constraints;

import java.time.Year;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class PromoCodeValidator extends AbstractValidator<ValidPromoCode, String> {

	@Override
	public void initialize(final ValidPromoCode annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final String promoCode, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (promoCode != null) {
			{
				boolean patternMatched = Pattern.matches("^[A-Z]{4}-[0-9]{2}$", promoCode);

				super.state(context, patternMatched, "*", "acme.validation.promo.error.message");
			}
			{
				boolean lastTwoDigitsMatchYear;

				try {
					int length = promoCode.length();
					String lastTwoDigitsString = promoCode.substring(length - 2, length);
					int lastTwoDigits = Integer.parseInt(lastTwoDigitsString);

					int year = Year.now().getValue() % 100;

					lastTwoDigitsMatchYear = lastTwoDigits == year;

				} catch (Error e) {
					lastTwoDigitsMatchYear = false;
				}

				super.state(context, lastTwoDigitsMatchYear, "*", "acme.validation.promo.last-digits.message");
			}
		}

		result = !super.hasErrors(context);

		return result;

	}
}
