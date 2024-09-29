package br.com.fiap.postech.adjt.checkout.controller.facade.OrderFacadeImp;

import br.com.fiap.postech.adjt.checkout.application.controller.facade.implementation.OrderFacadeImp;
import br.com.fiap.postech.adjt.checkout.application.dto.CheckoutDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.CheckoutResponseDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.OrderDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.PaymentDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.OrderItemDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.FieldDTO; // Certifique-se de importar o FieldDTO
import br.com.fiap.postech.adjt.checkout.application.mappers.CheckoutMapper;
import br.com.fiap.postech.adjt.checkout.application.mappers.OrderMapper;
import br.com.fiap.postech.adjt.checkout.domain.exception.AppException;
import br.com.fiap.postech.adjt.checkout.domain.model.enums.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderItemModel;
import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderModel;
import br.com.fiap.postech.adjt.checkout.domain.model.payment.CheckoutModel;
import br.com.fiap.postech.adjt.checkout.domain.usecase.CreateOrderUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderFacadeImpTests {

    private OrderMapper orderMapper;
    private CreateOrderUseCase orderUseCase;
    private CheckoutMapper checkoutMapper;
    private OrderFacadeImp orderFacade;

    @BeforeEach
    void setUp() {
        orderMapper = mock(OrderMapper.class);
        orderUseCase = mock(CreateOrderUseCase.class);
        checkoutMapper = mock(CheckoutMapper.class);
        orderFacade = new OrderFacadeImp(orderMapper, orderUseCase, checkoutMapper);
    }

    @Test
    void testCreateOrder() throws AppException {
        // Arrange
        FieldDTO fieldDTO = new FieldDTO("1234567890123456", "12", "25", "123", "Consumer Name");
        PaymentDTO paymentDTO = new PaymentDTO("credit_card", fieldDTO);
        CheckoutDTO checkoutDTO = new CheckoutDTO(
                "consumerIdValue", // Substitua pelo ID do consumidor real
                100.0,            // Valor total da compra
                "USD",            // Moeda
                paymentDTO        // Instância de PaymentDTO
        );

        CheckoutModel checkoutModel = new CheckoutModel();
        OrderModel orderModel = new OrderModel();
        orderModel.setOrderId(UUID.randomUUID());
        orderModel.setPaymentStatus(PaymentStatus.approved); // Supondo que você tenha esse enum

        when(checkoutMapper.toCheckoutModel(checkoutDTO)).thenReturn(checkoutModel);
        when(orderUseCase.createOrder(checkoutModel)).thenReturn(orderModel);

        // Act
        CheckoutResponseDTO response = orderFacade.createOrder(checkoutDTO);

        // Assert
        assertNotNull(response);
        assertEquals(orderModel.getOrderId().toString(), response.orderId()); // Usando o método gerado do record
        assertEquals(orderModel.getPaymentStatus().name(), response.status()); // Atualizado para corresponder ao novo nome
    }

    @Test
    void testGetOrderById() throws AppException {
        // Arrange
        UUID orderId = UUID.randomUUID(); // Gera um UUID válido
        List<OrderItemModel> itemModels = List.of(new OrderItemModel(orderId, 2L, "item1")); // Usando OrderItemModel
        OrderModel orderModel = new OrderModel();
        orderModel.setOrderId(orderId);
        orderModel.setPaymentStatus(PaymentStatus.approved);
        orderModel.setValue(100.0); // Define o valor
        orderModel.setItems(itemModels); // Define os itens

        // Converta OrderItemModel para OrderItemDTO
        List<OrderItemDTO> itemDTOs = itemModels.stream()
                .map(item -> new OrderItemDTO(item.getCodItem(), item.getQuantity())) // Ajuste conforme necessário
                .toList();

        OrderDTO orderDTO = new OrderDTO(orderId.toString(), "credit_card", 100.0, PaymentStatus.approved, itemDTOs); // Ajuste para OrderDTO

        when(orderUseCase.getOrderById(orderId.toString())).thenReturn(orderModel);
        when(orderMapper.toOrderDTO(orderModel)).thenReturn(orderDTO);

        // Act
        OrderDTO result = orderFacade.getOrderById(orderId.toString());

        // Assert
        assertNotNull(result);
        verify(orderUseCase).getOrderById(orderId.toString());
        verify(orderMapper).toOrderDTO(orderModel);
    }

    @Test
    void testGetOrderByCustomerId_whenOrdersExist() throws AppException {
        // Arrange
        String customerId = "customer123";
        List<OrderModel> orderModels = List.of(new OrderModel());
        when(orderUseCase.getOrderByConsumerId(customerId)).thenReturn(orderModels);

        // Criar um OrderDTO de exemplo
        List<OrderItemDTO> items = List.of(new OrderItemDTO("item1", 2L));
        OrderDTO orderDTO = new OrderDTO(
                UUID.randomUUID().toString(),
                "credit_card",
                100.0,
                PaymentStatus.approved,
                items
        );
        when(orderMapper.toOrderDTO(any())).thenReturn(orderDTO);

        // Act
        List<OrderDTO> result = orderFacade.getOrderByCustomerId(customerId);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(orderUseCase).getOrderByConsumerId(customerId);
    }

}
