package com.z.nativejpablocking.utils.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotEmptyIfNotNullValidator implements ConstraintValidator<NotEmptyIfNotNull, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        } else {
            return !value.isEmpty();
        }
    }
}
