
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.entities.claim.Claim;
import acme.entities.trackingLog.TrackingLog;

public class TrackingLogUpdateMomentValidator extends AbstractValidator<ValidTrackingLogUpdateMoment, TrackingLog> {

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		if (trackingLog == null || trackingLog.getClaim() == null || trackingLog.getLastUpdateMoment() == null)
			return true;

		Claim claim = trackingLog.getClaim();
		if (claim == null || claim.getRegistrationMoment() == null)
			return true;

		boolean isValid = trackingLog.getLastUpdateMoment().after(claim.getRegistrationMoment());

		if (!isValid) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("{acme.validation.trackingLog.updateMoment.message}").addPropertyNode("lastUpdateMoment").addConstraintViolation();
		}

		return isValid;
	}
}
