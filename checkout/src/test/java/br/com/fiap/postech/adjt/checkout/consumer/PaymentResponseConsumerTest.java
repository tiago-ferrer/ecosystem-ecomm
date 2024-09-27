package br.com.fiap.postech.adjt.checkout.consumer;

import br.com.fiap.postech.adjt.checkout.TestUtils;
import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentResponseDto;
import br.com.fiap.postech.adjt.checkout.entity.enums.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentResponseConsumerTest {

    @InjectMocks
    private PaymentResponseConsumer paymentResponseConsumer;

    @Mock
    private Message message;

    @Mock
    private MessageProperties messageProperties;

    @Test
    void testConsumePaymentResponseApproved() {
        when(message.getMessageProperties()).thenReturn(messageProperties);
        when(messageProperties.getCorrelationId()).thenReturn("");
        PaymentResponseDto dto = TestUtils.buildPaymentResponseDto();
        dto.setStatus(PaymentStatus.approved.toString());
        paymentResponseConsumer.consumePaymentResponse(dto, message);
        verify(message, times(1)).getMessageProperties();
    }


    @Test
    void testConsumePaymentResponseDeclined() {
        when(message.getMessageProperties()).thenReturn(messageProperties);
        when(messageProperties.getCorrelationId()).thenReturn("");
        PaymentResponseDto dto = TestUtils.buildPaymentResponseDto();
        dto.setStatus(PaymentStatus.declined.toString());
        paymentResponseConsumer.consumePaymentResponse(dto, message);
        verify(message, times(1)).getMessageProperties();
    }

    @Test
    void testConsumePaymentResponsePending() {
        when(message.getMessageProperties()).thenReturn(messageProperties);
        when(messageProperties.getCorrelationId()).thenReturn("");
        PaymentResponseDto dto = TestUtils.buildPaymentResponseDto();
        dto.setStatus(PaymentStatus.pending.toString());
        paymentResponseConsumer.consumePaymentResponse(dto, message);
        verify(message, times(1)).getMessageProperties();
    }



}
