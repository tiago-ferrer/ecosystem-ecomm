package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.dto.ExternalPaymentResponseDTO;
import br.com.fiap.postech.adjt.checkout.kafka.PaymentConsumer;
import br.com.fiap.postech.adjt.checkout.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentConsumerTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private OrderService orderService;

    @Mock
    private CartService cartService;

    @InjectMocks
    private PaymentConsumer paymentConsumer;

    private PaymentMessage paymentMessage;
    private Order order;
    private Checkout checkout;
    private ExternalPaymentResponseDTO approvedResponse;
    private ExternalPaymentResponseDTO declinedResponse;
    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configura o objeto de teste
        order = Order.builder()
                .orderId(UUID.randomUUID())
                .value(100)
                .currency(Currency.BRL)
                .paymentStatus(PaymentStatus.pending)
                .build();

        checkout = Checkout.builder()
                .consumerId(UUID.randomUUID())
                .paymentMethod(PaymentMethod.builder().build())
                .build();

        paymentMessage = new PaymentMessage(order, checkout);

        // Respostas simuladas da API externa
        approvedResponse = new ExternalPaymentResponseDTO(order.getOrderId().toString(), PaymentStatus.approved.toString());
        declinedResponse = new ExternalPaymentResponseDTO(order.getOrderId().toString(), PaymentStatus.declined.toString());

        // Configurar valor da apiKey e URL do serviço de pagamento
        ReflectionTestUtils.setField(paymentConsumer, "apiKeyValue", "test-api-key");
        ReflectionTestUtils.setField(paymentConsumer, "paymentServiceUrl", "http://fake-payment-service.com");
    }

    @Test
    void shouldApprovePaymentAndClearCart() {
        // Simula resposta de aprovação
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(ExternalPaymentResponseDTO.class)))
                .thenReturn(new ResponseEntity<>(approvedResponse, HttpStatus.OK));

        // Executa o método
        paymentConsumer.consumePayment(paymentMessage);

        // Verifica se o status do pedido foi atualizado para aprovado
        verify(orderService, times(1)).updateOrder(any(Order.class));
        assertEquals(PaymentStatus.approved, order.getPaymentStatus());

        // Verifica se o carrinho foi esvaziado
        verify(cartService, times(1)).clearCart(checkout.getConsumerId());
    }

    @Test
    void shouldDeclinePayment() {
        // Simula resposta de rejeição
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(ExternalPaymentResponseDTO.class)))
                .thenReturn(new ResponseEntity<>(declinedResponse, HttpStatus.OK));

        // Executa o método
        paymentConsumer.consumePayment(paymentMessage);

        // Verifica se o status do pedido foi atualizado para recusado
        verify(orderService, times(1)).updateOrder(any(Order.class));
        assertEquals(PaymentStatus.declined, order.getPaymentStatus());

        // Verifica se o carrinho não foi esvaziado
        verify(cartService, never()).clearCart(checkout.getConsumerId());
    }

    @Test
    void shouldHandleNullResponseBody() {
        // Simula uma resposta com corpo nulo
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(ExternalPaymentResponseDTO.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        // Executa o método
        paymentConsumer.consumePayment(paymentMessage);

        // Verifica se o status do pedido foi atualizado para recusado
        verify(orderService, times(1)).updateOrder(any(Order.class));
        assertEquals(PaymentStatus.declined, order.getPaymentStatus());

        // Verifica se o carrinho não foi esvaziado
        verify(cartService, never()).clearCart(checkout.getConsumerId());
    }
}