package com.stepaniuk.nerdysoft.validation.member;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.hibernate.validator.constraints.Length;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@Length(min = 1, max = 255)
public @interface Surname {
    String message() default "Surname must be between 1 and 255 characters long!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
