package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.exception.*;
import br.com.fiap.postech.adjt.checkout.model.Cart;
import br.com.fiap.postech.adjt.checkout.model.Item;
import br.com.fiap.postech.adjt.checkout.validation.UUIDValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CartValidatorTest {

    private UUIDValidator uuidValidator;
    private ConstraintValidatorContext context;

    private void invokeValidateCartItemList(Cart cart) throws Exception {
        Method method = CheckoutService.class.getDeclaredMethod("validateCartItemList", Cart.class);
        method.setAccessible(true);
        method.invoke(null, cart);
    }

    @BeforeEach
    void setUp() {
        uuidValidator = new UUIDValidator();
        context = mock(ConstraintValidatorContext.class);
        uuidValidator.initialize(null);
    }

    @Test
    void validateCartItemListShouldThrowEmptyCartExceptionWhenItemListIsEmpty() {

        Cart emptyCart = Cart.builder().build();
        emptyCart.setItems(Collections.emptyList());

        assertThrows(InvocationTargetException.class, () -> {
            invokeValidateCartItemList(emptyCart);
        });
    }

    @Test
    void validateCartItemListShouldThrowEmptyCartExceptionWhenItemListIsNull() {

        Cart nullCart = Cart.builder().build();
        nullCart.setItems(null);

        assertThrows(InvocationTargetException.class, () -> {
            invokeValidateCartItemList(nullCart);
        });
    }

    @Test
    void validateCartItemListShouldNotThrowExceptionWhenItemListIsNotEmpty() {

        Cart validCart = Cart.builder().build();
        validCart.setItems(Arrays.asList(Item.builder().build()));

        assertDoesNotThrow(() -> {
            invokeValidateCartItemList(validCart);
        });
    }

    @Test
    void isValidShouldReturnTrueForValidUUID() {

        String validUUID = "dcb3f098-6cf2-4c89-bb5e-b0fcb0babe49";

        boolean result = uuidValidator.isValid(validUUID, context);

        assertTrue(result);
    }

    @Test
    void isValidShouldThrowExceptionForInvalidUUID() {

        String invalidUUID = "invalid-uuid";

        assertThrows(InvalidConsumerIdFormatException.class, () -> {
            uuidValidator.isValid(invalidUUID, context);
        });
    }

    @Test
    void testInvalidOrderUuidFormatException() {
        InvalidOrderUuidFormatException exception = assertThrows(
                InvalidOrderUuidFormatException.class,
                () -> {
                    throw new InvalidOrderUuidFormatException();
                }
        );
        assertEquals("Invalid orderId format", exception.getMessage());
    }

    @Test
    void testInvalidPaymentMethodException() {

        InvalidPaymentMethodException exception = assertThrows(
                InvalidPaymentMethodException.class,
                () -> {
                    throw new InvalidPaymentMethodException();
                }
        );
        assertEquals("Invalid payment information", exception.getMessage());
    }
    @Test
    void testPaymentProcessingException() {

        PaymentProcessingException exception = assertThrows(
                PaymentProcessingException.class,
                () -> {
                    throw new PaymentProcessingException();
                }
        );
        assertEquals("Payment processing failed", exception.getMessage());
    }

}

