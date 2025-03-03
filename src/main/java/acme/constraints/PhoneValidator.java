/*
 * PhoneValidator.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.constraints;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.datatypes.Phone;

@Validator
public class PhoneValidator extends AbstractValidator<ValidPhone, Phone> {

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidPhone annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Phone phone, final ConstraintValidatorContext context) {
		// HINT: phone can be null
		assert context != null;

		boolean result;
		boolean isNull;

		isNull = phone == null || phone.getCountryCode() == null && phone.getAreaCode() == null && phone.getNumber() == null;

		if (!isNull) {
			{
				Integer countryCode;
				boolean inRange;

				countryCode = phone.getCountryCode();
				inRange = countryCode == null || countryCode >= 1 && countryCode <= 999;
				super.state(context, inRange, "countryCode", "acme.validation.phone.message");
			}
			{
				Integer areaCode;
				boolean inRange;

				areaCode = phone.getAreaCode();
				inRange = areaCode == null || areaCode >= 1 && areaCode <= 99999;
				super.state(context, inRange, "areaCode", "acme.validation.phone.message");
			}
			{
				String number;
				boolean matchesPattern;

				number = phone.getNumber();
				matchesPattern = number != null && Pattern.matches("^\\+?\\d{6,15}$", number);
				super.state(context, matchesPattern, "number", "acme.validation.phone.message");
			}

		}

		result = !super.hasErrors(context);

		return result;
	}

}
