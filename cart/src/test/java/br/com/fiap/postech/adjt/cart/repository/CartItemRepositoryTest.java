package br.com.fiap.postech.adjt.cart.repository;

import br.com.fiap.postech.adjt.cart.model.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

        Item item = new Item();
        item.setConsumerId(consumerId);
        item.setItemId(itemId);
        itemRepository.save(item);

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
        Long cartId = 2L;

        Item item = new Item();
        item.setCart(cartRepository.findById(cartId).orElse(null));
        itemRepository.save(item);

        List<Item> items = itemRepository.findByCart_CartId(cartId);
        assertFalse(items.isEmpty());
    }

    @Test
    void testFindByCart_CartIdNotExists() {
        Long cartId = 999L;

        List<Item> items = itemRepository.findByCart_CartId(cartId);
        assertTrue(items.isEmpty());
    }


}