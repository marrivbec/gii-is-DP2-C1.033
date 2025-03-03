
package acme.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CargoWeightValidator implements ConstraintValidator<ValidCargoWeight, Integer> {

	private static final int	MIN_WEIGHT	= 2000;
	private static final int	MAX_WEIGHT	= 50000;


	@Override
	public boolean isValid(final Integer weight, final ConstraintValidatorContext context) {
		if (weight == null)
			return false;
		return weight >= CargoWeightValidator.MIN_WEIGHT && weight <= CargoWeightValidator.MAX_WEIGHT;
	}
}
