package br.com.fiap.postech.adjt.checkout.domain.exception;

import br.com.fiap.postech.adjt.checkout.domain.exception.constants.ErrorConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorConstantsTest {

    @Test
    void testOrderIdFormatInvalid() {
        assertEquals("Invalid orderId format", ErrorConstants.ORDER_ID_FORMAT_INVALID);
    }

    @Test
    void testUserIdFormatInvalid() {
        assertEquals("Invalid consumerId format", ErrorConstants.USER_ID_FORMAT_INVALID);
    }

    @Test
    void testCartItemsEmpty() {
        assertEquals("Cart does not contain any item", ErrorConstants.CART_ITEMS_EMPTY);
    }

    @Test
    void testOrderIdNotFound() {
        assertEquals("Order not found", ErrorConstants.ORDER_ID_NOT_FOUND);
    }
}
