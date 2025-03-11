
package acme.entities.flight;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidLongText;
import acme.constraints.ValidShortText;
import acme.entities.leg.LegRepository;
import acme.realms.employee.AirlineManager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Automapped
	@ValidShortText
	private String				tag;

	@Mandatory
	@Automapped
	@Valid
	private Boolean				selfTransfer;

	@Mandatory
	@Automapped
	@ValidMoney
	private Money				cost;

	@Optional
	@Automapped
	@ValidLongText
	private String				description;

	// Derived attributes -----------------------------------------------------


	@Transient
	public LocalDateTime getScheduledDeparture() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		List<LocalDateTime> results = repository.findScheduledDepartureByFlightId(this.getId());
		return results.isEmpty() ? null : results.get(0);
	}

	@Transient
	public LocalDateTime getScheduledArrival() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		List<LocalDateTime> results = repository.findScheduledArrivalByFlightId(this.getId());
		return results.isEmpty() ? null : results.get(0);
	}

	@Transient
	public String getOriginCity() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		List<String> results = repository.findOriginCityByFlightId(this.getId());
		return results.isEmpty() ? null : results.get(0);
	}

	@Transient
	public String getDestinationCity() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		List<String> results = repository.findDestinationCityByFlightId(this.getId());
		return results.isEmpty() ? null : results.get(0);
	}

	@Transient
	public Integer getLayovers() {
		LegRepository repository = SpringHelper.getBean(LegRepository.class);
		return repository.findLayoversByFlightId(this.getId());
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private AirlineManager airlineManager;

}
