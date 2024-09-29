package br.com.fiap.postech.adjt.checkout.model.dto.request;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

class CheckoutRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validateConsumerId_ValidUUID_NoExceptionThrown() {
        // Arrange
        CheckoutRequest request = new CheckoutRequest("123e4567-e89b-12d3-a456-426614174000", 100, "USD", new PaymentMethodRequest());

        // Act & Assert
        assertDoesNotThrow(() -> request.validateConsumerId());
    }

    @Test
    void validateConsumerId_InvalidUUID_ThrowsIllegalArgumentException() {
        // Arrange
        CheckoutRequest request = new CheckoutRequest("invalid-uuid", 100, "USD", new PaymentMethodRequest());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, request::validateConsumerId);
        assertEquals("Invalid consumerId format", exception.getMessage());
    }

    @Test
    void validateAmount_AmountLessThanOne_ShouldFailValidation() {
        // Arrange
        CheckoutRequest request = new CheckoutRequest("123e4567-e89b-12d3-a456-426614174000", 0, "USD", new PaymentMethodRequest());

        // Act
        Set<ConstraintViolation<CheckoutRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Amount must be greater than or equal to 0")));
    }

    @Test
    void validateCurrency_BlankCurrency_ShouldFailValidation() {
        // Arrange
        CheckoutRequest request = new CheckoutRequest("123e4567-e89b-12d3-a456-426614174000", 100, "", new PaymentMethodRequest());

        // Act
        Set<ConstraintViolation<CheckoutRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Currency cannot be blank")));
    }

    @Test
    void validatePaymentMethod_NullPaymentMethod_ShouldFailValidation() {
        // Arrange
        CheckoutRequest request = new CheckoutRequest("123e4567-e89b-12d3-a456-426614174000", 100, "USD", null);

        // Act
        Set<ConstraintViolation<CheckoutRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Payment method cannot be null")));
    }

    @Test
    void validateConsumerId_InvalidFormat_ShouldFailValidation() {
        // Arrange
        CheckoutRequest request = new CheckoutRequest("invalid-uuid-format", 100, "USD", new PaymentMethodRequest());

        // Act
        Set<ConstraintViolation<CheckoutRequest>> violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Invalid consumerId format")));
    }

    @Test
    void validateConsumerId_ValidFormat_ShouldPassValidation() {
        // Arrange
        CheckoutRequest request = new CheckoutRequest("123e4567-e89b-12d3-a456-426614174000", 100, "USD", new PaymentMethodRequest());

        // Act
        Set<ConstraintViolation<CheckoutRequest>> violations = validator.validate(request);

        // Assert
        assertTrue(violations.isEmpty());
    }
}