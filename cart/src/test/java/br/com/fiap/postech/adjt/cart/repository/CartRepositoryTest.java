package br.com.fiap.postech.adjt.cart.repository;

import br.com.fiap.postech.adjt.cart.model.Cart;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Test
    void testFindByConsumerIdExists() {
        UUID consumerId = UUID.randomUUID();

        Cart cart = new Cart();
        cart.setConsumerId(consumerId);
        cart.setItems(new ArrayList<>());

        cartRepository.save(cart);

        Optional<Cart> foundCart = cartRepository.findByConsumerId(consumerId);
        assertTrue(foundCart.isPresent());
        assertEquals(cart.getConsumerId(), foundCart.get().getConsumerId());
    }

    @Test
    void testFindByConsumerIdNotExists() {
        UUID consumerId = UUID.randomUUID();
        Optional<Cart> foundCart = cartRepository.findByConsumerId(consumerId);
        assertFalse(foundCart.isPresent());
    }

}