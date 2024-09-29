package br.com.fiap.postech.adjt.cart.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class CartTests {

    private Cart cart;
    private UUID consumerId;
    private List<Item> items;

    @BeforeEach
    public void setup() {
        consumerId = UUID.randomUUID();
        items = new ArrayList<>();
        cart = new Cart(consumerId, items);
    }

    @Test
    public void testCartInitialization() {
        assertNotNull(cart);
        assertEquals(consumerId, cart.getConsumerId());
        assertEquals(items, cart.getItems());
    }

    @Test
    public void testSetConsumerId() {
        UUID newConsumerId = UUID.randomUUID();
        cart.setConsumerId(newConsumerId);
        assertEquals(newConsumerId, cart.getConsumerId());
    }

    @Test
    public void testSetItems() {
        List<Item> newItems = new ArrayList<>();
        cart.setItems(newItems);
        assertEquals(newItems, cart.getItems());
    }


    @Test
    public void testRemoveItem() {
        Item item = new Item();
        cart.getItems().add(item);
        cart.getItems().remove(item);
        assertFalse(cart.getItems().contains(item));
    }

    @Test
    public void testClearCart() {
        Item item = new Item();
        cart.getItems().add(item);
        cart.getItems().clear();
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    public void testAddItem() {
        Item item = new Item("product1", 2);
        cart.getItems().add(item);
        assertTrue(cart.getItems().contains(item));
    }

    @Test
    public void testRemoveNonExistentItem() {
        Item nonExistentItem = new Item("product2", 1);
        boolean result = cart.getItems().remove(nonExistentItem);
        assertFalse(result); // Remover um item que não existe deve retornar false
    }

    @Test
    public void testSetItemsWithNull() {
        cart.setItems(null);
        assertNull(cart.getItems()); // Espera-se que items seja null
    }

    @Test
    public void testSetItemsWithEmptyList() {
        List<Item> emptyItems = new ArrayList<>();
        cart.setItems(emptyItems);
        assertEquals(emptyItems, cart.getItems());
    }

    @Test
    public void testAddDuplicateItem() {
        Item item = new Item("product1", 2);
        cart.getItems().add(item);
        cart.getItems().add(item); // Adicionando o mesmo item novamente
        assertEquals(2, cart.getItems().size()); // O tamanho deve ser 2
    }

}
