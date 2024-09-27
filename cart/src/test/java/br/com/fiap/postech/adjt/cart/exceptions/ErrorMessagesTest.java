package br.com.fiap.postech.adjt.cart.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorMessagesTest {

    @Test
    void testInvalidConsumerIdFormat() {
        assertEquals("Invalid consumerId format", ErrorMessages.INVALID_CONSUMER_ID_FORMAT);
    }

    @Test
    void testEmptyCart() {
        assertEquals("Empty cart", ErrorMessages.EMPTY_CART);
    }

    @Test
    void testInvalidItemId() {
        assertEquals("Invalid itemId", ErrorMessages.INVALID_ITEM_ID);
    }

    @Test
    void testInvalidItemQuantity() {
        assertEquals("Invalid itemId quantity", ErrorMessages.INVALID_ITEM_QUANTITY);
    }

    @Test
    void testItemAddedSuccessfully() {
        assertEquals("Item added to cart successfully", ErrorMessages.ITEM_ADDED_SUCCESSFULLY);
    }

    @Test
    void testItemRemovedSuccessfully() {
        assertEquals("Item removed from cart successfully", ErrorMessages.ITEM_REMOVED_SUCCESSFULLY);
    }

    @Test
    void testItemIncrementedSuccessfully() {
        assertEquals("Item incremented successfully", ErrorMessages.ITEM_INCREMENTED_SUCCESSFULLY);
    }

    @Test
    void testCartClearedSuccessfully() {
        assertEquals("Items removed from cart successfully", ErrorMessages.CART_CLEARED_SUCCESSFULLY);
    }
}
