/*
 * LongListValidator.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, pero no se garantiza para ningún propósito
 * particular. El propietario de los derechos de autor no ofrece ninguna garantía ni
 * representación, ni acepta ninguna responsabilidad con respecto a ellos.
 */

package acme.constraints;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class LongListValidator extends AbstractValidator<ValidLongList, List<String>> {

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidLongList annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final List<String> value, final ConstraintValidatorContext context) {
		// HINT: value can be null
		assert context != null;

		boolean result = true;

		if (value == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else
			for (String item : value)
				if (item == null || !this.isValidString(item, context)) {
					result = false;
					break;
				}

		return result;
	}

	private boolean isValidString(final String text, final ConstraintValidatorContext context) {
		boolean valid = text.length() >= 1 && text.length() <= 255;
		if (!valid)
			super.state(context, false, "*", "acme.validation.text.message");
		return valid;
	}
}
