
package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.trackingLog.TrackingLog;
import acme.entities.trackingLog.TrackingLogRepository;

@Validator
public class TrackingLogResolutionPercentageValidator extends AbstractValidator<ValidTrackingLogResolutionPercentage, TrackingLog> {

	@Autowired
	private TrackingLogRepository trackingLogRepository;


	@Override
	public boolean isValid(final TrackingLog newTrackingLog, final ConstraintValidatorContext context) {
		if (newTrackingLog == null || newTrackingLog.getClaim() == null || newTrackingLog.getResolutionPercentage() == null)
			return true;

		List<TrackingLog> existingLogs = this.trackingLogRepository.findLastTrackingLogByClaimId(newTrackingLog.getClaim().getId());

		if (existingLogs.isEmpty())
			return true;

		TrackingLog previousLog = existingLogs.get(existingLogs.size() - 1);

		Integer newId = newTrackingLog.getId();
		if (newId != null) {
			int currentIndex = -1;
			for (int i = 0; i < existingLogs.size(); i++)
				if (newId.equals(existingLogs.get(i).getId())) {
					currentIndex = i;
					break;
				}
			if (currentIndex > 0)
				previousLog = existingLogs.get(currentIndex - 1);
			else if (currentIndex == 0) {
				for (TrackingLog log : existingLogs)
					if (log.getId() != newTrackingLog.getId() && !newTrackingLog.getResolutionPercentage().equals(100.0) && log.getResolutionPercentage().equals(newTrackingLog.getResolutionPercentage()))
						return false;
				return true;
			}

		}

		for (TrackingLog log : existingLogs)
			if (log.getId() != newTrackingLog.getId() && !newTrackingLog.getResolutionPercentage().equals(100.0) && log.getResolutionPercentage().equals(newTrackingLog.getResolutionPercentage()))
				return false;

		if (previousLog != null)
			return this.validatePercentage(newTrackingLog, previousLog, context);

		return true;
	}

	private boolean validatePercentage(final TrackingLog newLog, final TrackingLog previousLog, final ConstraintValidatorContext context) {
		Double previousPercentage = previousLog.getResolutionPercentage();
		Double newPercentage = newLog.getResolutionPercentage();
		boolean isValid;

		if (previousPercentage != 100)
			isValid = newPercentage > previousPercentage;
		else
			isValid = newPercentage >= previousPercentage;

		super.state(context, isValid, "resolutionPercentage", "acme.validation.trackingLog.resolutionPercentage.message");

		return isValid;
	}
}
