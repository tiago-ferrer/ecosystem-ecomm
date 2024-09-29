package br.com.fiap.postech.adjt.checkout.consumer;

import br.com.fiap.postech.adjt.checkout.TestUtils;
import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentResponseDto;
import br.com.fiap.postech.adjt.checkout.integracao.PaymentServiceAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PaymentRequestConsumerTest {

    @InjectMocks
    private PaymentRequestConsumer paymentRequestConsumer;

    @Mock
    private PaymentServiceAPI paymentServiceAPI;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private WebClient webClient;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(paymentServiceAPI, "webClient", webClient);
    }

    @Test
    void testConsumePaymentRequest() {
        Message message = new Message("teste".getBytes());
        Mono<PaymentResponseDto> mono = Mono.just(TestUtils.buildPaymentResponseDto());
        when(paymentServiceAPI.processPayment(Mockito.any())).thenReturn(mono);
        paymentRequestConsumer.consumePaymentRequest(TestUtils.buildPaymentRequestDto(), message);
        verify(rabbitTemplate, times(1))
                .convertAndSend(Mockito.anyString(), Mockito.any(Message.class));
    }

}
