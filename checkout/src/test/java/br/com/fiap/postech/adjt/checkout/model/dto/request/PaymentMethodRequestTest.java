package br.com.fiap.postech.adjt.checkout.model.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMethodRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidation_WhenAllFieldsAreValid() {
        // Arrange
        PaymentFieldsRequest fields = new PaymentFieldsRequest("1234567890123456", "12", "2025", "123", "John Doe");
        PaymentMethodRequest paymentMethodRequest = new PaymentMethodRequest("credit_card", fields);

        // Act
        Set<ConstraintViolation<PaymentMethodRequest>> violations = validator.validate(paymentMethodRequest);

        // Assert
        assertTrue(violations.isEmpty(), "There should be no validation errors");
    }

    @Test
    void shouldFailValidation_WhenTypeIsBlank() {
        // Arrange
        PaymentFieldsRequest fields = new PaymentFieldsRequest("1234567890123456", "12", "2025", "123", "John Doe");
        PaymentMethodRequest paymentMethodRequest = new PaymentMethodRequest("", fields);

        // Act
        Set<ConstraintViolation<PaymentMethodRequest>> violations = validator.validate(paymentMethodRequest);

        // Assert
        assertFalse(violations.isEmpty(), "There should be validation errors");
        assertEquals(1, violations.size());
        assertEquals("Payment type cannot be blank", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidation_WhenFieldsIsNull() {
        // Arrange
        PaymentMethodRequest paymentMethodRequest = new PaymentMethodRequest("credit_card", null);

        // Act
        Set<ConstraintViolation<PaymentMethodRequest>> violations = validator.validate(paymentMethodRequest);

        // Assert
        assertFalse(violations.isEmpty(), "There should be validation errors");
        assertEquals(1, violations.size());
        assertEquals("Payment fields cannot be null", violations.iterator().next().getMessage());
    }
}