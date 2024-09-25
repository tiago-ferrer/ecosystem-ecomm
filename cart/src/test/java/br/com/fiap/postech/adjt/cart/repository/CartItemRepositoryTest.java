package br.com.fiap.postech.adjt.cart.repository;

import br.com.fiap.postech.adjt.cart.model.Cart;
import br.com.fiap.postech.adjt.cart.model.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository itemRepository;
    @Autowired
    private CartRepository cartRepository;

    @Test
    void testFindByConsumerIdAndItemIdExists() {

        UUID consumerId = UUID.randomUUID();
        Long itemId = 1L;

        Cart cart = new Cart();
        cart.setConsumerId(consumerId);
        cart.setItems(new ArrayList<>());
        cartRepository.save(cart);

        Item item = new Item();
        item.setItemId(itemId);
        item.setConsumerId(consumerId);
        item.setQuantity(1);
        item.setPrice(new BigDecimal("10.00"));
        item.setCart(cart);
        itemRepository.save(item);

        cart.getItems().add(item);
        cartRepository.save(cart);

        Optional<Item> foundItem = itemRepository.findByConsumerIdAndItemId(consumerId, itemId);
        assertTrue(foundItem.isPresent());
        assertEquals(itemId, foundItem.get().getItemId());
    }


    @Test
    void testFindByConsumerIdAndItemIdNotExists() {
        UUID consumerId = UUID.randomUUID();
        Long itemId = 999L;

        Optional<Item> foundItem = itemRepository.findByConsumerIdAndItemId(consumerId, itemId);
        assertFalse(foundItem.isPresent());
    }

    @Test
    void testFindByCart_CartIdExists() {

        UUID consumerId = UUID.randomUUID();
        Long itemId = 1L;

        Cart cart = new Cart();
        cart.setConsumerId(consumerId);
        cart.setItems(new ArrayList<>());
        cartRepository.save(cart);

        Item item = new Item();
        item.setItemId(itemId);
        item.setConsumerId(consumerId);
        item.setQuantity(1);
        item.setPrice(new BigDecimal("10.00"));
        item.setCart(cart);
        itemRepository.save(item);

        cart.getItems().add(item);
        cartRepository.save(cart);

        List<Item> items = itemRepository.findByCart_CartId(cart.getCartId());
        assertFalse(items.isEmpty());
    }

    @Test
    void testFindByCart_CartIdNotExists() {
        Long cartId = 999L;

        List<Item> items = itemRepository.findByCart_CartId(cartId);
        assertTrue(items.isEmpty());
    }


}