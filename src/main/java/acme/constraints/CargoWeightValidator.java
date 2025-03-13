
package acme.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CargoWeightValidator implements ConstraintValidator<ValidCargoWeight, Double> {

	private static final double	MIN_WEIGHT	= 2000.0;
	private static final double	MAX_WEIGHT	= 50000.0;


	@Override
	public boolean isValid(final Double weight, final ConstraintValidatorContext context) {
		if (weight == null)
			return false;
		return weight >= CargoWeightValidator.MIN_WEIGHT && weight <= CargoWeightValidator.MAX_WEIGHT;
	}
}
