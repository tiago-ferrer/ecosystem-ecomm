package br.com.fiap.postech.adjt.checkout.service.impl;

import br.com.fiap.postech.adjt.checkout.TestUtils;
import br.com.fiap.postech.adjt.checkout.dto.order.OrderResponseDto;
import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentResponseDto;
import br.com.fiap.postech.adjt.checkout.entity.enums.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.exception.InvalidOrderIdException;
import br.com.fiap.postech.adjt.checkout.repository.FieldRepository;
import br.com.fiap.postech.adjt.checkout.repository.PaymentRepository;
import br.com.fiap.postech.adjt.checkout.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentServiceImpl;

    @Mock
    private PaymentRepository repository;

    @Mock
    private OrderService service;

    @Mock
    private FieldRepository fieldRepository;

    @Test
    void testCreatePayment() {
        paymentServiceImpl.createPayment(TestUtils.buildPayment());
        verify(repository, times(1)).save(Mockito.any());
    }

    @Test
    void testValidarPaymentWhenPaymentIdIsPresentAndPaymentLocate() {
        OrderResponseDto dto = TestUtils.buildOrderResponseDto();
        dto.setPaymentStatus(PaymentStatus.approved);
        when(service.getById(Mockito.any())).thenReturn(dto);
        paymentServiceImpl.validarPayment(TestUtils.buildPaymentResponseDto());
        verify(service, times(1)).updateStatus(Mockito.any());
    }

    @Test
    void testValidarPaymentWhenPaymentIdIsPresentAndPaymentLocateStatusEquals() {
        when(service.getById(Mockito.any())).thenReturn(TestUtils.buildOrderResponseDto());
        paymentServiceImpl.validarPayment(TestUtils.buildPaymentResponseDto());
        verify(service, times(1)).getById(Mockito.any());
        verify(service, never()).updateStatus(Mockito.any());
    }

    @Test
    void testValidarPaymentWhenNotExist() {
        when(service.getById(Mockito.any())).thenReturn(null);
        paymentServiceImpl.validarPayment(TestUtils.buildPaymentResponseDto());
        verify(service, never()).updateStatus(Mockito.any());
    }

    @Test
    void testValidarPaymentWhenPaymentIdIsNull() {
        PaymentResponseDto dto = new PaymentResponseDto();
        String msgError = assertThrows(InvalidOrderIdException.class, () ->
                paymentServiceImpl.validarPayment(dto)).getMessage();
        assertEquals("Invalid orderId format", msgError);
    }



}
