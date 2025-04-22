
package acme.entities.claim;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidClaimRegistrationMoment;
import acme.entities.leg.Leg;
import acme.entities.trackingLog.Indicator;
import acme.entities.trackingLog.TrackingLog;
import acme.realms.employee.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidClaimRegistrationMoment
public class Claim extends AbstractEntity {
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@ValidEmail
	@Automapped
	private String				passengerEmail;

	@Mandatory
	@ValidString(min = 1, max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@Automapped
	private ClaimType			type;

	@Mandatory
	// HINT: @Valid by default.
	@Automapped
	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------


	@Transient
	public String indicator() {
		String result;
		ClaimRepository repository;
		TrackingLog trackingLog;

		repository = SpringHelper.getBean(ClaimRepository.class);
		trackingLog = repository.findLastTrackingLogByClaimId(this.getId()).stream().findFirst().orElse(null);
		if (trackingLog == null)
			result = null;
		else {
			Indicator indicator = trackingLog.getIndicator();
			if (indicator.equals(Indicator.ACCEPTED))
				result = "ACCEPTED";
			else if (indicator.equals(Indicator.REJECTED))
				result = "REJECTED";
			else
				result = "PENDING";
		}
		return result;
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AssistanceAgent	assistanceAgents;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Leg				leg;
}
