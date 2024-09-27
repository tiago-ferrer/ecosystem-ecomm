package br.com.fiap.postech.adjt.checkout.dataprovider.mapper;

import br.com.fiap.postech.adjt.checkout.dataprovider.database.entity.OrderEntity;
import br.com.fiap.postech.adjt.checkout.dataprovider.database.mapper.OrderMapper; // Certifique-se de importar a interface correta
import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderModel;
import br.com.fiap.postech.adjt.checkout.domain.model.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderMapperTest {

    private OrderMapper orderMapper;

    @BeforeEach
    void setUp() {
        orderMapper = Mappers.getMapper(OrderMapper.class); // Isso deve estar correto se a interface está configurada
    }

    @Test
    void testToOrderEntity() {
        // Given
        OrderModel orderModel = new OrderModel();
        orderModel.setOrderId(UUID.randomUUID()); // Usar UUID para orderId
        orderModel.setPaymentStatus(PaymentStatus.pending); // Verifique a enumeração
        orderModel.setConsumerId("customer-123");
        orderModel.setPaymentType("credit card");
        orderModel.setValue(100.0);
        orderModel.setItems(Collections.emptyList()); // Adicione itens conforme necessário

        // When
        OrderEntity orderEntity = orderMapper.toOrderEntity(orderModel);

        // Then
        assertEquals(orderModel.getOrderId(), orderEntity.getOrderId());
        assertEquals(orderModel.getPaymentStatus(), orderEntity.getPaymentStatus());
        assertEquals(orderModel.getConsumerId(), orderEntity.getConsumerId());
        assertEquals(orderModel.getPaymentType(), orderEntity.getPaymentType());
        assertEquals(orderModel.getValue(), orderEntity.getValue());
    }

    @Test
    void testToOrderModel() {
        // Given
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(UUID.randomUUID()); // Usar UUID para orderId
        orderEntity.setPaymentStatus(PaymentStatus.pending); // Verifique a enumeração
        orderEntity.setConsumerId("customer-123");
        orderEntity.setPaymentType("credit card");
        orderEntity.setValue(100.0);
        orderEntity.setItems(Collections.emptyList()); // Adicione itens conforme necessário

        // When
        OrderModel orderModel = orderMapper.toOrderModel(orderEntity);

        // Then
        assertEquals(orderEntity.getOrderId(), orderModel.getOrderId());
        assertEquals(orderEntity.getPaymentStatus(), orderModel.getPaymentStatus());
        assertEquals(orderEntity.getConsumerId(), orderModel.getConsumerId());
        assertEquals(orderEntity.getPaymentType(), orderModel.getPaymentType());
        assertEquals(orderEntity.getValue(), orderModel.getValue());
    }
}
