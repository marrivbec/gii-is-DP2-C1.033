
package acme.entities.airline;

import java.time.LocalDate;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Airline extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Automapped
	private String				name;

	@Mandatory
	@Automapped
	private String				iata;

	@Mandatory
	@Automapped
	private String				web;

	@Mandatory
	@Automapped
	private AirlineType			tipo;

	@Mandatory
	private LocalDate			fechaFundacion;

	@Optional
	@Automapped
	private String				correo;

	@Optional
	@Automapped
	private String				telefono;


	public Airline() {
		// TODO Auto-generated constructor stub
	}

}
