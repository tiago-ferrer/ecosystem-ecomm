package br.com.fiap.postech.adjt.checkout.validation;

import br.com.fiap.postech.adjt.checkout.exception.InvalidConsumerIdFormatException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.UUID;

public class UUIDValidator implements ConstraintValidator<ValidUUID, String> {
    @Override
    public void initialize(ValidUUID constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            throw new InvalidConsumerIdFormatException();
        }
    }
}