
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

@Constraint(validatedBy = CargoWeightValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface ValidCargoWeight {

	String message() default "{acme.validation.cargo.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
