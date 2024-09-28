package br.com.fiap.postech.adjt.checkout.model.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PaymentFieldsRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validateNumber_BlankNumber_ShouldFailValidation() {
        // Arrange
        PaymentFieldsRequest request = new PaymentFieldsRequest("", "12", "2025", "123", "John Doe");

        // Act
        Set<ConstraintViolation<PaymentFieldsRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Card number cannot be blank")));
    }

    @Test
    void validateExpirationMonth_BlankExpirationMonth_ShouldFailValidation() {
        // Arrange
        PaymentFieldsRequest request = new PaymentFieldsRequest("1234567890123456", "", "2025", "123", "John Doe");

        // Act
        Set<ConstraintViolation<PaymentFieldsRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Expiration month cannot be blank")));
    }

    @Test
    void validateExpirationYear_BlankExpirationYear_ShouldFailValidation() {
        // Arrange
        PaymentFieldsRequest request = new PaymentFieldsRequest("1234567890123456", "12", "", "123", "John Doe");

        // Act
        Set<ConstraintViolation<PaymentFieldsRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Expiration year cannot be blank")));
    }

    @Test
    void validateCvv_BlankCvv_ShouldFailValidation() {
        // Arrange
        PaymentFieldsRequest request = new PaymentFieldsRequest("1234567890123456", "12", "2025", "", "John Doe");

        // Act
        Set<ConstraintViolation<PaymentFieldsRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("CVV cannot be blank")));
    }

    @Test
    void validateName_BlankName_ShouldFailValidation() {
        // Arrange
        PaymentFieldsRequest request = new PaymentFieldsRequest("1234567890123456", "12", "2025", "123", "");

        // Act
        Set<ConstraintViolation<PaymentFieldsRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Cardholder name cannot be blank")));
    }

    @Test
    void validateAllFields_ValidFields_ShouldPassValidation() {
        // Arrange
        PaymentFieldsRequest request = new PaymentFieldsRequest("1234567890123456", "12", "2025", "123", "John Doe");

        // Act
        Set<ConstraintViolation<PaymentFieldsRequest>> violations = validator.validate(request);

        // Assert
        assertTrue(violations.isEmpty());
    }
}