package br.com.fiap.postech.adjt.checkout.producer;


import br.com.fiap.postech.adjt.checkout.TestUtils;
import br.com.fiap.postech.adjt.checkout.config.PaymentProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.function.StreamBridge;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentEventProducerTest {

    @InjectMocks
    private PaymentEventProducer paymentEventProducer;

    @Mock
    private StreamBridge streamBridge;

    @Mock
    private PaymentProperties paymentProperties;

    @Test
    void testSendPaymentCreateEvent() {
        when(paymentProperties.getPaymentCreatedChannel()).thenReturn("channel");
        paymentEventProducer.sendPaymentCreateEvent(TestUtils.buildPaymentRequestDto());
        verify(streamBridge, times(1)).send(Mockito.anyString(), Mockito.any());
    }


}
