package com.ifsc.ctds.stinghen.recycle_it_api.security.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<StrongPassword, String> {

    private static final String PASSWORD_PATTERN =
            "^(?=(?:.*\\d){2,})(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{12}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return value.matches(PASSWORD_PATTERN);
    }
}