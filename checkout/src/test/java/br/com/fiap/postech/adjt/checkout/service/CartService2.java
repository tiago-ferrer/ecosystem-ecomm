package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.model.Cart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CartServiceTest {

    private CartService cartService;
    private CartRepository cartRepository;
    private UUID consumerId;

    @BeforeEach
    void setUp() {
        cartRepository = new InMemoryCartRepository();
        cartService = new CartService(cartRepository);
        consumerId = UUID.randomUUID();
    }

    @Test
    void getCart_ShouldReturnCart() {
        Cart cart = new Cart(consumerId, null);
        cartRepository.saveCart(cart);

        Cart result = cartService.getCart(consumerId);

        assertEquals(consumerId, result.getConsumerId());
    }

    @Test
    void clearCart_ShouldClearCart() {
        cartRepository.saveCart(new Cart(consumerId, null));

        cartService.clearCart(consumerId);

        assertNull(cartRepository.findByConsumerId(consumerId));
    }
}