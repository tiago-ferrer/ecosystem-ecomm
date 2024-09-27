package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.domain.exception.AppException;
import br.com.fiap.postech.adjt.checkout.domain.exception.constants.ErrorConstants;
import br.com.fiap.postech.adjt.checkout.domain.gateway.CartGateway;
import br.com.fiap.postech.adjt.checkout.domain.gateway.OrderGateway;
import br.com.fiap.postech.adjt.checkout.domain.gateway.PaymentGateway;
import br.com.fiap.postech.adjt.checkout.domain.model.cart.CartModel;
import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderModel;
import br.com.fiap.postech.adjt.checkout.domain.model.payment.CheckoutModel;
import br.com.fiap.postech.adjt.checkout.domain.model.payment.PaymentMethodModel;
import br.com.fiap.postech.adjt.checkout.domain.usecase.CreateOrderUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateOrderUseCaseTests {

    @InjectMocks
    private CreateOrderUseCase createOrderUseCase;

    @Mock
    private OrderGateway orderGateway;

    @Mock
    private CartGateway cartGateway;

    @Mock
    private PaymentGateway paymentGateway;

    private CheckoutModel checkoutModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        checkoutModel = new CheckoutModel();
        checkoutModel.setConsumerId(UUID.randomUUID().toString());
        checkoutModel.setAmount(100.0);
        // Adicione outras propriedades necessárias
    }

    @Test
    void testCreateOrder_CartNotFound() {
        // Arrange
        when(cartGateway.getCartByConsumerId(any())).thenThrow(new RuntimeException());

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> createOrderUseCase.createOrder(checkoutModel));
        assertEquals(ErrorConstants.USER_ID_FORMAT_INVALID, exception.getMessage());
    }

    @Test
    void testCreateOrder_Success() throws AppException {
        // Arrange
        CartModel cartModel = new CartModel();
        CartModel.CartItensModel item = new CartModel.CartItensModel("item1", 2L);
        cartModel.setItems(Collections.singletonList(item));
        when(cartGateway.getCartByConsumerId(checkoutModel.getConsumerId())).thenReturn(cartModel);

        // Crie um PaymentMethodModel e defina no CheckoutModel
        PaymentMethodModel paymentMethod = new PaymentMethodModel();
        paymentMethod.setType("CREDIT_CARD"); // Defina o tipo de pagamento conforme sua enumeração ou constante
        checkoutModel.setPaymentMethod(paymentMethod); // Defina o PaymentMethodModel no CheckoutModel

        OrderModel orderModel = new OrderModel();
        orderModel.setOrderId(UUID.randomUUID());
        when(orderGateway.createNewOrder(any(OrderModel.class))).thenReturn(orderModel);

        // Act
        OrderModel result = createOrderUseCase.createOrder(checkoutModel);

        // Assert
        assertNotNull(result);
        assertEquals(orderModel.getOrderId(), result.getOrderId());
        verify(cartGateway).emptyCartByConsumerId(checkoutModel.getConsumerId());
        verify(paymentGateway).processPayment(checkoutModel, orderModel.getOrderId());
    }


    @Test
    void testGetOrderByConsumerId_Success() throws AppException {
        // Arrange
        String consumerId = UUID.randomUUID().toString();
        when(orderGateway.findByConsumerId(consumerId)).thenReturn(Collections.singletonList(new OrderModel()));

        // Act
        List<OrderModel> orders = createOrderUseCase.getOrderByConsumerId(consumerId);

        // Assert
        assertFalse(orders.isEmpty());
        verify(orderGateway).findByConsumerId(consumerId);
    }

    @Test
    void testGetOrderByConsumerId_InvalidUUID() {
        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> createOrderUseCase.getOrderByConsumerId("invalid-uuid"));
        assertEquals(ErrorConstants.USER_ID_FORMAT_INVALID, exception.getMessage());
    }

    @Test
    void testGetOrderById_Success() throws AppException {
        // Arrange
        String orderId = UUID.randomUUID().toString();
        OrderModel orderModel = new OrderModel();
        when(orderGateway.findOrderModelById(UUID.fromString(orderId))).thenReturn(Optional.of(orderModel));

        // Act
        OrderModel result = createOrderUseCase.getOrderById(orderId);

        // Assert
        assertEquals(orderModel, result);
        verify(orderGateway).findOrderModelById(UUID.fromString(orderId));
    }

    @Test
    void testGetOrderById_OrderNotFound() {
        // Arrange
        String orderId = UUID.randomUUID().toString();
        when(orderGateway.findOrderModelById(UUID.fromString(orderId))).thenReturn(Optional.empty());

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> createOrderUseCase.getOrderById(orderId));
        assertEquals(ErrorConstants.ORDER_ID_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testGetOrderById_InvalidUUID() {
        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> createOrderUseCase.getOrderById("invalid-uuid"));
        assertEquals(ErrorConstants.ORDER_ID_FORMAT_INVALID, exception.getMessage());
    }
}
