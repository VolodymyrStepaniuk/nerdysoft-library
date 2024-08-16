package com.stepaniuk.nerdysoft.validation.book;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AuthorValidator implements ConstraintValidator<Author, String> {

    @Override
    public void initialize(Author author) {
        ConstraintValidator.super.initialize(author);
    }

    @Override
    public boolean isValid(String authorName, ConstraintValidatorContext context) {

        if (authorName == null || authorName.isEmpty()) {
            return true;
        }

        String regex = "^[A-Z][a-z]* [A-Z][a-z]*$";
        return authorName.matches(regex);
    }
}