package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.application.dto.CheckoutDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.CheckoutResponseDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.FieldDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.PaymentDTO;
import br.com.fiap.postech.adjt.checkout.domain.exception.AppException;
import br.com.fiap.postech.adjt.checkout.domain.gateway.OrderGateway;
import br.com.fiap.postech.adjt.checkout.domain.model.enums.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderModel;
import br.com.fiap.postech.adjt.checkout.domain.usecase.CreateOrderUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CreateOrderUseCaseTests {

    @InjectMocks
    private CreateOrderUseCase createOrderUseCase;

    @Mock
    private OrderGateway orderGateway;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder_Success() throws AppException {
        // Dados de teste
        CheckoutDTO checkoutDTO = new CheckoutDTO(
                "ec6f3e6b-cf54-49fb-915b-8079f651b161",
                1050.0,
                "BRL",
                new PaymentDTO(
                        "br_credit_card",
                        new FieldDTO(
                                "4111111111111111",
                                "12",
                                "25",
                                "255",
                                "John Doe"
                        )
                )
        );

        OrderModel savedOrder = new OrderModel(
                UUID.fromString("ec6f3e6b-cf54-49fb-915b-8079f651b161"),
                PaymentStatus.declined,
                "ec6f3e6b-cf54-49fb-915b-8079f651b161",
                "Credit_card",
                100.0,
                new ArrayList<>()
        );
        CheckoutResponseDTO expectedResponse = new CheckoutResponseDTO(
                "ec6f3e6b-cf54-49fb-915b-8079f651b161",
                "declined"
        );

        // Configurações dos mocks
        when(orderGateway.createNewOrder(any(OrderModel.class))).thenReturn(savedOrder);

    }

}
