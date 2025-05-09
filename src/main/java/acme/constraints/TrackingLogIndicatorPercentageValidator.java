
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.trackingLog.Indicator;
import acme.entities.trackingLog.TrackingLog;

@Validator
public class TrackingLogIndicatorPercentageValidator extends AbstractValidator<ValidTrackingLogIndicatorPercentage, TrackingLog> {

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {

		Double percentage = trackingLog.getResolutionPercentage();
		Indicator indicator = trackingLog.getIndicator();

		if (percentage != null && percentage == 100.0) {
			if (indicator != Indicator.ACCEPTED && indicator != Indicator.REJECTED) {
				this.state(context, false, "indicator", "acme.validation.trackingLog.IndicatorPercentage.message");
				return false;
			}
		} else if (indicator == Indicator.ACCEPTED || indicator == Indicator.REJECTED) {
			this.state(context, false, "indicator", "acme.validation.trackingLog.IndicatorPercentagePending.message");
			return false;
		}

		return true;
	}
}
