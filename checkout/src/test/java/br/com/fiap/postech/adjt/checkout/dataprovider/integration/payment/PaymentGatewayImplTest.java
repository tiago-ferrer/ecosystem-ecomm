package br.com.fiap.postech.adjt.checkout.dataprovider.integration.payment;

import br.com.fiap.postech.adjt.checkout.dataprovider.database.entity.OrderEntity;
import br.com.fiap.postech.adjt.checkout.dataprovider.database.entity.OrderItemEntity;
import br.com.fiap.postech.adjt.checkout.dataprovider.database.repository.OrderRepository;
import br.com.fiap.postech.adjt.checkout.domain.model.enums.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderStatusModel;
import br.com.fiap.postech.adjt.checkout.domain.model.payment.CheckoutModel;
import br.com.fiap.postech.adjt.checkout.domain.model.payment.FieldModel;
import br.com.fiap.postech.adjt.checkout.domain.model.payment.PaymentMethodModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;


public class PaymentGatewayImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private PaymentGatewayImpl paymentGateway;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentGateway = new PaymentGatewayImpl(orderRepository, null);

        // Configura o WebClient mock
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.method(any())).thenReturn(requestBodyUriSpec);
//        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
//        when(requestHeadersSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

    }

    @Test
    void testProcessPaymentSuccess() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UUID orderIdItem = UUID.randomUUID();
        PaymentStatus paymentStatus = PaymentStatus.pending;
        OrderStatusModel orderStatusModel = new OrderStatusModel("1", paymentStatus);
        FieldModel fieldModel = new FieldModel("1","teste", "teste", "teste", "te0");
        PaymentMethodModel paymentMethodModel = new PaymentMethodModel("teste", fieldModel);
        CheckoutModel checkoutModel = new CheckoutModel("1", "teste", 10.5, "test", paymentMethodModel);


        OrderEntity orderEntity = new OrderEntity(orderId, paymentStatus, "test", "test", 10.5, List.of(new OrderItemEntity()));
        OrderItemEntity orderItemEntity = new OrderItemEntity(orderIdItem, "teste", 1L, 10.5, orderEntity);

        when(responseSpec.bodyToMono(OrderStatusModel.class)).thenReturn(Mono.just(orderStatusModel));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity));

        // Act
        Mono<Void> result = Mono.fromRunnable(() -> paymentGateway.processPayment(checkoutModel, orderId));

        // Assert
        StepVerifier.create(result)
                .expectSubscription()
                .expectComplete()
                .verify();
    }
}
