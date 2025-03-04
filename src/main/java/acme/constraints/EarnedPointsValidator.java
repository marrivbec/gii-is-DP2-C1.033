
package acme.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EarnedPointsValidator implements ConstraintValidator<ValidEarnedPoints, Integer> {

	@Override
	public boolean isValid(final Integer points, final ConstraintValidatorContext context) {
		if (points == null)
			return true;

		return points >= 0 && points <= 500000;
	}
}
