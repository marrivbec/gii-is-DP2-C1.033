
package acme.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ScoreValidator implements ConstraintValidator<ValidScore, Double> {

	@Override
	public boolean isValid(final Double score, final ConstraintValidatorContext context) {
		if (score == null)
			return true;

		if (score < 0 || score > 10)
			return false;

		return true;
	}
}
