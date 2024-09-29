package br.com.fiap.postech.adjt.checkout.model.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRequestTest {

	private Validator validator;

	@BeforeEach
	void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	void shouldPassValidation_WhenAllFieldsAreValid() {
		// Arrange
		PaymentMethodRequest paymentMethod = new PaymentMethodRequest("credit_card",
				new PaymentFieldsRequest("1234567890123456", "12", "2025", "123", "John Doe"));
		PaymentRequest paymentRequest = new PaymentRequest("order123", 100, "USD", paymentMethod);

		// Act
		Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(paymentRequest);

		// Assert
		assertTrue(violations.isEmpty(), "There should be no validation errors");
	}

	@Test
	void shouldFailValidation_WhenOrderIdIsEmpty() {
		// Arrange
		PaymentMethodRequest paymentMethod = new PaymentMethodRequest("credit_card",
				new PaymentFieldsRequest("1234567890123456", "12", "2025", "123", "John Doe"));
		PaymentRequest paymentRequest = new PaymentRequest("", 100, "USD", paymentMethod);

		// Act
		Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(paymentRequest);

		// Assert
		assertFalse(violations.isEmpty(), "There should be validation errors");
		assertEquals(1, violations.size());
		assertEquals("Order ID cannot be blank", violations.iterator().next().getMessage()); // Atualize a mensagem
																								// esperada aqui
	}

	@Test
	void shouldFailValidation_WhenCurrencyIsBlank() {
		// Arrange
		PaymentMethodRequest paymentMethod = new PaymentMethodRequest("credit_card",
				new PaymentFieldsRequest("1234567890123456", "12", "2025", "123", "John Doe"));
		PaymentRequest paymentRequest = new PaymentRequest("order123", 100, "", paymentMethod);

		// Act
		Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(paymentRequest);

		// Assert
		assertFalse(violations.isEmpty(), "There should be validation errors");
		assertEquals(1, violations.size());
		assertEquals("Currency cannot be blank", violations.iterator().next().getMessage());
	}

	@Test
	void shouldFailValidation_WhenPaymentMethodIsNull() {
		// Arrange
		PaymentRequest paymentRequest = new PaymentRequest("order123", 100, "USD", null);

		// Act
		Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(paymentRequest);

		// Assert
		assertFalse(violations.isEmpty(), "There should be validation errors");
		assertEquals(1, violations.size());
		assertEquals("Payment method cannot be null", violations.iterator().next().getMessage());
	}
}