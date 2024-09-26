package br.com.fiap.postech.adjt.cart.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

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
}
