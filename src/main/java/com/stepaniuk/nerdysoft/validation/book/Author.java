package com.stepaniuk.nerdysoft.validation.book;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { AuthorValidator.class })
public @interface Author {
    String message() default "Author name must consist of a first and last name, both starting with a capital letter, and separated by a space!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
