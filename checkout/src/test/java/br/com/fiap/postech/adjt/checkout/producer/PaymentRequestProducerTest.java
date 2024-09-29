package br.com.fiap.postech.adjt.checkout.producer;

import br.com.fiap.postech.adjt.checkout.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PaymentRequestProducerTest {

    @InjectMocks
    private PaymentRequestProducer paymentRequestProducer;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Test
    void test() {
        paymentRequestProducer.sendPaymentRequest(
                TestUtils.buildPaymentRequestDto(), "correlationId");
        verify(rabbitTemplate, times(1))
                .convertAndSend(Mockito.anyString(), Mockito.any(Message.class));
    }

}
