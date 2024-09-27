package br.com.fiap.postech.adjt.checkout.consumer;

import br.com.fiap.postech.adjt.checkout.TestUtils;
import br.com.fiap.postech.adjt.checkout.dto.order.OrderResponseDto;
import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentResponseDto;
import br.com.fiap.postech.adjt.checkout.entity.enums.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.integracao.PaymentServiceAPI;
import br.com.fiap.postech.adjt.checkout.service.OrderService;
import br.com.fiap.postech.adjt.checkout.service.impl.PaymentConfirmedService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentConsumerTest {

    @InjectMocks
    private PaymentConsumer paymentConsumer;

    @Mock
    private PaymentServiceAPI paymentServiceAPI;

    @Mock
    private OrderService orderService;

    @Mock
    private PaymentConfirmedService paymentConfirmedService;

    @Test
    void testReceiveWhenPaymentApproved() {
        PaymentResponseDto paymentResponseDto = TestUtils.buildPaymentResponseDto();
        paymentResponseDto.setStatus(PaymentStatus.approved.toString());
        when(paymentServiceAPI.sendPayment(Mockito.any())).thenReturn(paymentResponseDto);
        when(orderService.getById(Mockito.any())).thenReturn(TestUtils.buildOrderResponseDto());
        paymentConsumer.receive(TestUtils.buildPaymentRequestDto());
        verify(orderService, times(1))
                .updateStatusByStatusName(Mockito.any(), Mockito.eq(PaymentStatus.approved));
    }

    @Test
    void testReceiveWhenPaymentDeclined() {
        PaymentResponseDto paymentResponseDto = TestUtils.buildPaymentResponseDto();
        paymentResponseDto.setStatus(PaymentStatus.declined.toString());
        when(paymentServiceAPI.sendPayment(Mockito.any())).thenReturn(paymentResponseDto);
        when(orderService.getById(Mockito.any())).thenReturn(TestUtils.buildOrderResponseDto());
        paymentConsumer.receive(TestUtils.buildPaymentRequestDto());
        verify(orderService, times(1))
                .updateStatusByStatusName(Mockito.any(), Mockito.eq(PaymentStatus.declined));
    }

    @Test
    void testReceiveWhenException() {
        when(paymentServiceAPI.sendPayment(Mockito.any())).thenThrow(RuntimeException.class);
        paymentConsumer.receive(TestUtils.buildPaymentRequestDto());
        verify(orderService, times(1))
                .updateStatusByStatusName(Mockito.any(), Mockito.eq(PaymentStatus.declined));
    }

}
