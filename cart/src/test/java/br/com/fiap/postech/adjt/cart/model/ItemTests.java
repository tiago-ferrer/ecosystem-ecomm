package br.com.fiap.postech.adjt.cart.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class ItemTests {

    @Test
    public void testItemCreation() {
        Item item = new Item("product123", 5);

        assertThat(item).isNotNull();
        assertThat(item.getProductId()).isEqualTo("product123");
        assertThat(item.getQuantity()).isEqualTo(5);
    }

    @Test
    public void testItemDefaultConstructor() {
        Item item = new Item();

        assertThat(item).isNotNull();
        assertThat(item.getProductId()).isNull();
        assertThat(item.getQuantity()).isZero();
    }

    @Test
    public void testSettersAndGetters() {
        Item item = new Item();
        item.setProductId("product456");
        item.setQuantity(3);

        assertThat(item.getProductId()).isEqualTo("product456");
        assertThat(item.getQuantity()).isEqualTo(3);
    }

    @Test
    public void testItemEquality() {
        Item item1 = new Item("product123", 5);
        Item item2 = new Item("product123", 5);

        assertThat(item1).isEqualTo(item2);
    }

    @Test
    public void testItemHashCode() {
        Item item1 = new Item("product123", 5);
        Item item2 = new Item("product123", 5);

        assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
    }

    @Test
    public void testItemCreationValid() {
        Item item = new Item("product1", 2);
        assertNotNull(item);
        assertEquals("product1", item.getProductId());
        assertEquals(2, item.getQuantity());
    }

    @Test
    public void testItemCreationWithNullProductId() {
        Item item = new Item(null, 2);
        assertNull(item.getProductId()); // Não podemos validar, mas podemos garantir que a construção não lança exceções
        assertEquals(2, item.getQuantity());
    }

    @Test
    public void testItemCreationWithZeroQuantity() {
        Item item = new Item("product1", 0);
        assertEquals("product1", item.getProductId());
        assertEquals(0, item.getQuantity()); // Quantidade 0 deve ser permitida, mas deve ser tratado na lógica do carrinho
    }
}
