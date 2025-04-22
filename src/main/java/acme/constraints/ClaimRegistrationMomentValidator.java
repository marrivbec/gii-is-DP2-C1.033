
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.entities.claim.Claim;
import acme.entities.leg.Leg;

public class ClaimRegistrationMomentValidator extends AbstractValidator<ValidClaimRegistrationMoment, Claim> {

	@Override
	public boolean isValid(final Claim claim, final ConstraintValidatorContext context) {
		if (claim == null || claim.getRegistrationMoment() == null || claim.getLeg() == null || claim.getLeg().getScheduledArrival() == null)
			return true;

		Leg leg = claim.getLeg();
		boolean isValid = claim.getRegistrationMoment().after(leg.getScheduledArrival());

		if (!isValid) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("{acme.validation.claim.registrationMoment.message}").addPropertyNode("registrationMoment").addConstraintViolation();
		}

		return isValid;
	}
}
