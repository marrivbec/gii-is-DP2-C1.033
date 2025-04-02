
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.trackingLog.Indicator;
import acme.entities.trackingLog.TrackingLog;

@Validator
public class TrackingLogResolutionValidator extends AbstractValidator<ValidTrackingLogResolution, TrackingLog> {

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		Indicator indicator = trackingLog.getIndicator();

		if (!(indicator == Indicator.ACCEPTED || indicator == Indicator.REJECTED))
			return true;

		boolean isValid = trackingLog.getResolution() != null && !trackingLog.getResolution().trim().isEmpty();

		if (!isValid)
			this.state(context, false, "resolution", "acme.validation.trackingLog.resolution.message");

		return isValid;
	}
}
