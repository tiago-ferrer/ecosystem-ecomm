package br.com.fiap.postech.adjt.checkout.consumer;

import br.com.fiap.postech.adjt.checkout.TestUtils;
import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentRequestDto;
import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentResponseDto;
import br.com.fiap.postech.adjt.checkout.entity.enums.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.integracao.PaymentServiceAPI;
import br.com.fiap.postech.adjt.checkout.service.OrderService;
import br.com.fiap.postech.adjt.checkout.service.impl.PaymentConfirmedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentCreatedEventListnerTest {

    @InjectMocks
    private PaymentCreatedEventListner paymentCreatedEventListner;

    @Mock
    private OrderService service;

    @Mock
    private PaymentServiceAPI paymentService;

    @Mock
    private WebClient webClient;

    @Mock
    private ModelMapper mapper;

    @Mock
    private PaymentConfirmedService paymentConfirmedService;

    @Mock
    private Mono<PaymentResponseDto> mono;

    @Mock
    private PaymentRequestDto paymentRequestDto;


    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(paymentService, "webClient", webClient);
    }


    @Test
    void testAcceptWhenNotStatusApproved() {
        PaymentResponseDto paymentResponseDto = TestUtils.buildPaymentResponseDto();
        paymentResponseDto.setStatus(PaymentStatus.approved.toString());
        Mono<PaymentResponseDto> mono = Mono.just(paymentResponseDto);
        when(paymentService.processPayment(Mockito.any())).thenReturn(mono);
        when(service.getById(Mockito.any())).thenReturn(TestUtils.buildOrderResponseDto());
        paymentCreatedEventListner.accept(TestUtils.buildPaymentRequestDto());
        verify(service, times(1))
                .updateStatusByStatusName(Mockito.any(), Mockito.eq(PaymentStatus.approved));
    }

    @Test
    void testAcceptWhenNotStatusDeclined() {
        PaymentResponseDto paymentResponseDto = TestUtils.buildPaymentResponseDto();
        paymentResponseDto.setStatus(PaymentStatus.declined.toString());
        Mono<PaymentResponseDto> mono = Mono.just(paymentResponseDto);
        when(paymentService.processPayment(Mockito.any())).thenReturn(mono);
        when(service.getById(Mockito.any())).thenReturn(TestUtils.buildOrderResponseDto());
        paymentCreatedEventListner.accept(TestUtils.buildPaymentRequestDto());
        verify(service, times(1))
                .updateStatusByStatusName(Mockito.any(), Mockito.eq(PaymentStatus.declined));
    }

    @Test
    void testAcceptWhenNotStatusUpdate() {
        Mono<PaymentResponseDto> mono = Mono.just(TestUtils.buildPaymentResponseDto());
        when(paymentService.processPayment(Mockito.any())).thenReturn(mono);
        when(service.getById(Mockito.any())).thenReturn(TestUtils.buildOrderResponseDto());
        paymentCreatedEventListner.accept(TestUtils.buildPaymentRequestDto());
        verify(service, never()).updateStatusByStatusName(Mockito.any(), Mockito.any());
    }
}
