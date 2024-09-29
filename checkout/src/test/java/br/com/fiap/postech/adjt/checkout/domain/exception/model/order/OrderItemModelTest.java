package br.com.fiap.postech.adjt.checkout.domain.exception.model.order;

import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderItemModel;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderItemModelTest {

    @Test
    public void testBuilder() {
        UUID id = UUID.randomUUID();
        Long quantity = 5L;
        String codItem = "ITEM123";

        OrderItemModel orderItem = OrderItemModel.builder()
                .id(id)
                .quantity(quantity)
                .codItem(codItem)
                .build();

        assertThat(orderItem).isNotNull();
        assertThat(orderItem.getId()).isEqualTo(id);
        assertThat(orderItem.getQuantity()).isEqualTo(quantity);
        assertThat(orderItem.getCodItem()).isEqualTo(codItem);
    }

    @Test
    public void testNoArgsConstructor() {
        OrderItemModel orderItem = new OrderItemModel();

        assertThat(orderItem).isNotNull();
        assertThat(orderItem.getId()).isNull();
        assertThat(orderItem.getQuantity()).isNull();
        assertThat(orderItem.getCodItem()).isNull();
    }

    @Test
    public void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        Long quantity = 10L;
        String codItem = "ITEM456";

        OrderItemModel orderItem = new OrderItemModel(id, quantity, codItem);

        assertThat(orderItem).isNotNull();
        assertThat(orderItem.getId()).isEqualTo(id);
        assertThat(orderItem.getQuantity()).isEqualTo(quantity);
        assertThat(orderItem.getCodItem()).isEqualTo(codItem);
    }

    @Test
    public void testGettersAndSetters() {
        OrderItemModel orderItem = new OrderItemModel();
        UUID id = UUID.randomUUID();
        Long quantity = 3L;
        String codItem = "ITEM789";

        orderItem.setId(id);
        orderItem.setQuantity(quantity);
        orderItem.setCodItem(codItem);

        assertThat(orderItem.getId()).isEqualTo(id);
        assertThat(orderItem.getQuantity()).isEqualTo(quantity);
        assertThat(orderItem.getCodItem()).isEqualTo(codItem);
    }
}
