package br.com.fiap.postech.adjt.model.entity;

import br.com.fiap.postech.adjt.cart.model.entity.Cart;
import br.com.fiap.postech.adjt.cart.model.entity.CartItem;
import org.junit.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CartItemTest {

    @Test
    public void testConstrutorWithNoParameters_ValidConstrutor_ShouldSetCorrectly() {
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(10);
        assertNotNull(cartItem);
        assertEquals(10,cartItem.getQuantity());
    }

    @Test
    public void testConstrutorWithParameters_ValidConstrutorWithoutCart_ShouldSetCorrectly() {
        CartItem cartItem = new CartItem(1L,10);

        assertNotNull(cartItem);
        assertEquals(1L,cartItem.getItemId());
        assertEquals(10,cartItem.getQuantity());
    }

    @Test
    public void testConstrutorWithParameters_ValidConstrutorWithCart_ShouldSetCorrectly() {
        UUID customerId = UUID.randomUUID();
        CartItem cartItem = new CartItem(1L,10, new Cart(customerId));

        assertNotNull(cartItem);
        assertEquals(1L,cartItem.getItemId());
        assertEquals(10,cartItem.getQuantity());
    }

}
