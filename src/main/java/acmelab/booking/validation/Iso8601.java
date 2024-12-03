package acmelab.booking.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = Iso8601Validator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Iso8601 {
    String message() default "Invalid ISO-8601 date format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
