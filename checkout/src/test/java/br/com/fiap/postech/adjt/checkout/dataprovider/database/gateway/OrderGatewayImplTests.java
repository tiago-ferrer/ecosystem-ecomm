package br.com.fiap.postech.adjt.checkout.dataprovider.database.gateway;

import br.com.fiap.postech.adjt.checkout.dataprovider.database.entity.OrderEntity;
import br.com.fiap.postech.adjt.checkout.dataprovider.database.entity.OrderItemEntity;
import br.com.fiap.postech.adjt.checkout.dataprovider.database.mapper.OrderMapper;
import br.com.fiap.postech.adjt.checkout.dataprovider.database.repository.OrderRepository;
import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderGatewayImplTests {

    @InjectMocks
    private OrderGatewayImpl orderGateway;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private br.com.fiap.postech.adjt.checkout.application.mappers.OrderMapper orderMapperUnder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createNewOrder_ShouldSaveAndReturnOrderModel() {
        // Arrange
        OrderModel orderModel = new OrderModel(); // Preencha com dados de teste
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setItems(new ArrayList<>()); // Inicializa a lista de itens
        when(orderMapper.toOrderEntity(orderModel)).thenReturn(orderEntity);
        when(orderRepository.save(orderEntity)).thenReturn(orderEntity);
        when(orderMapper.toOrderModel(orderEntity)).thenReturn(orderModel);

        // Act
        OrderModel result = orderGateway.createNewOrder(orderModel);

        // Assert
        assertEquals(orderModel, result);
        verify(orderMapper).toOrderEntity(orderModel);
        verify(orderRepository).save(orderEntity);
        verify(orderMapper).toOrderModel(orderEntity);
    }

    @Test
    void findByConsumerId_ShouldReturnListOfOrderModels() {
        // Arrange
        String consumerId = "someConsumerId";
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setItems(new ArrayList<>()); // Inicializa a lista de itens
        List<OrderEntity> orderEntities = Collections.singletonList(orderEntity);
        OrderModel orderModel = new OrderModel(); // Preencha com dados de teste
        when(orderRepository.findByConsumerId(consumerId)).thenReturn(orderEntities);
        when(orderMapperUnder.toOrderModel(orderEntity)).thenReturn(orderModel);

        // Act
        List<OrderModel> result = orderGateway.findByConsumerId(consumerId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(orderModel, result.get(0));
        verify(orderRepository).findByConsumerId(consumerId);
    }

    @Test
    void findOrderModelById_ShouldReturnOrderModelIfExists() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setItems(new ArrayList<>()); // Inicializa a lista de itens
        OrderModel orderModel = new OrderModel(); // Preencha com dados de teste
        when(orderRepository.findByOrderId(uuid)).thenReturn(Optional.of(orderEntity));
        when(orderMapper.toOrderModel(orderEntity)).thenReturn(orderModel);

        // Act
        Optional<OrderModel> result = orderGateway.findOrderModelById(uuid);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(orderModel, result.get());
        verify(orderRepository).findByOrderId(uuid);
    }

    @Test
    void findOrderModelById_ShouldReturnEmptyIfNotFound() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        when(orderRepository.findByOrderId(uuid)).thenReturn(Optional.empty());

        // Act
        Optional<OrderModel> result = orderGateway.findOrderModelById(uuid);

        // Assert
        assertFalse(result.isPresent());
        verify(orderRepository).findByOrderId(uuid);
    }
}
