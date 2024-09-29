package br.com.fiap.postech.adjt.model.entity;

import br.com.fiap.postech.adjt.cart.controller.exception.InvalidConsumerIdFormatException;
import br.com.fiap.postech.adjt.cart.model.entity.Cart;
import br.com.fiap.postech.adjt.cart.model.entity.CartItem;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CartTest {

    @Test
    public void testSetCartId_ValidValue_ShouldSetCorrectly() {
        Cart cart = new Cart();
        cart.setId(1L);
        assertEquals(1L, cart.getId());
    }

    @Test
    public void testSetCartCustomerId_ValidValue_ShouldSetCorrectly() {
        Cart cart = new Cart();
        UUID customerId = UUID.randomUUID();
        cart.setCustomerId(customerId);
        assertEquals(customerId, cart.getCustomerId());
    }

    @Test
    public void testItemConstructor_ValidCart_ShouldSetCorrectly() {
        UUID customerId = UUID.randomUUID();
        Cart cart = new Cart(customerId);

        assertEquals(customerId, cart.getCustomerId());
    }

    @Test
    public void testItemConstructorWithParameters_ValidCart_ShouldSetCorrectly() {
        UUID customerId = UUID.randomUUID();
        Cart cart = new Cart(1L,customerId);

        assertEquals(customerId, cart.getCustomerId());
    }

    @Test
    public void testAddItem_validItems_ShouldAddCorrectly() {
        CartItem cartItem = new CartItem(1L,10);
        UUID customerId = UUID.randomUUID();
        Cart cart = new Cart(customerId);

        cart.addItem(cartItem);

        assertEquals(cart.getItems().getFirst().getItemId(),cartItem.getItemId());
        assertEquals(cart.getItems().getFirst().getQuantity(),cartItem.getQuantity());
    }

    @Test
    public void testAddMoreItems_validItems_ShouldAddCorrectly() {
        CartItem cartItem = new CartItem(1L,10);
        UUID customerId = UUID.randomUUID();
        Cart cart = new Cart(customerId);

        cart.addItem(cartItem);
        cart.addItem(cartItem);

        assertEquals(cart.getItems().getFirst().getItemId(),cartItem.getItemId());
        assertEquals(cart.getItems().getFirst().getQuantity(),cartItem.getQuantity());
        assertEquals(20,cart.getItems().getLast().getQuantity());
    }

    @Test
    public void testRemoveItem_validItems_ShouldRemoveCorrectly() {
        CartItem cartItem = new CartItem(1L,1);
        UUID customerId = UUID.randomUUID();
        Cart cart = new Cart(customerId);

        cart.addItem(cartItem);
        cart.removeItem(1L);

        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    public void testRemoveAllItems_validItems_ShouldRemoveCorrectly() {
        CartItem cartItem = new CartItem(1L,10);
        UUID customerId = UUID.randomUUID();
        Cart cart = new Cart(customerId);

        cart.addItem(cartItem);
        cart.removeItem(1L);

        assertEquals(cart.getItems().getFirst().getItemId(),cartItem.getItemId());
        assertEquals(9,cart.getItems().getFirst().getQuantity());
    }

    @Test
    public void testRemoveItem_invalidId_shouldThrowIllegalArgumentException() {
        CartItem cartItem = new CartItem(1L,10);
        UUID customerId = UUID.randomUUID();
        Cart cart = new Cart(customerId);

        cart.addItem(cartItem);

        assertThrows(InvalidConsumerIdFormatException.class, () -> cart.removeItem(2L));
    }

    @Test
    public void testIncrementItem_validItems_ShouldAddCorrectly() {
        CartItem cartItem = new CartItem(1L,10);
        UUID customerId = UUID.randomUUID();
        Cart cart = new Cart(customerId);

        cart.addItem(cartItem);
        cart.incrementItem(1L);

        assertEquals(cart.getItems().getFirst().getItemId(),cartItem.getItemId());
        assertEquals(11,cart.getItems().getFirst().getQuantity());
    }

    @Test
    public void testIncrementItem_invalidItems_shouldThrowIllegalArgumentException() {
        CartItem cartItem = new CartItem(1L,10);
        UUID customerId = UUID.randomUUID();
        Cart cart = new Cart(customerId);

        cart.addItem(cartItem);

        assertThrows(IllegalArgumentException.class, () -> cart.incrementItem(2L));
    }

    @Test
    public void testValidateCartIsEmpty_validCall_shouldThrowIllegalArgumentException() {
        UUID customerId = UUID.randomUUID();
        Cart cart = new Cart(customerId);

        assertThrows(IllegalArgumentException.class, cart::checkIfCartIsEmpty);
    }

    @Test
    public void testValidateCartIsNotEmpty_validCall_shouldThrowIllegalArgumentException() {
        UUID customerId = UUID.randomUUID();
        Cart cart = new Cart(customerId);
        CartItem cartItem = new CartItem(1L,10);
        cart.addItem(cartItem);

        cart.checkIfCartIsEmpty();
    }

    @Test
    public void testClearItems_validItems_ShouldClearCorrectly() {
        CartItem cartItem = new CartItem(1L,10);
        UUID customerId = UUID.randomUUID();
        Cart cart = new Cart(customerId);

        cart.addItem(cartItem);
        cart.clear();

        assertTrue(cart.getItems().isEmpty());
    }

}
