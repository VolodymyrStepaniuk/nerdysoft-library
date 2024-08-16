package com.stepaniuk.nerdysoft.validation.book;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { TitleValidator.class })
public @interface Title {
    String message() default "Title must be between 3 and 255 characters long and start with a capital letter!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
