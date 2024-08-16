package com.stepaniuk.nerdysoft.validation.book;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TitleValidator implements ConstraintValidator<Title, String> {

    @Override
    public void initialize(Title title) {
        ConstraintValidator.super.initialize(title);
    }

    @Override
    public boolean isValid(String title, ConstraintValidatorContext constraintValidatorContext) {
        if (title == null || title.isEmpty()) {
            return true;
        }

        if (title.length() < 3 || title.length() > 255) {
            return false;
        }

        return Character.isUpperCase(title.charAt(0));
    }
}