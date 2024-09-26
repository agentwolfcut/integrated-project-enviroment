package dev.backendintegratedproject.validations.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import dev.backendintegratedproject.validations.validators.StatusExistsValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StatusExistsValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface StatusExists {
    String message() default "Status with such ID does not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
