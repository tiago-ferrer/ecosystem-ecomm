package br.com.fiap.postech.adjt.checkout.service.impl;

import br.com.fiap.postech.adjt.checkout.TestUtils;
import br.com.fiap.postech.adjt.checkout.consumer.ApiPayment;
import br.com.fiap.postech.adjt.checkout.dto.checkout.CheckoutConsumerDto;
import br.com.fiap.postech.adjt.checkout.entity.payment.Payment;
import br.com.fiap.postech.adjt.checkout.exception.CartConsumerException;
import br.com.fiap.postech.adjt.checkout.producer.PaymentEventProducer;
import br.com.fiap.postech.adjt.checkout.producer.PaymentProducer;
import br.com.fiap.postech.adjt.checkout.repository.CheckoutRepository;
import br.com.fiap.postech.adjt.checkout.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceImplTest {

    @InjectMocks
    private CheckoutServiceImpl checkoutServiceImpl;

    @Mock
    private CheckoutRepository repository;

    @Mock
    private OrderService service;

    @Mock
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentProducer paymentProducer;

    @Mock
    private ModelMapper mapper;


    @Test
    void testCreateCheckoutSuccess() {
        when(paymentService.createPayment(Mockito.any())).thenReturn(TestUtils.buildPayment());
        when(service.createOrder(Mockito.any())).thenReturn(TestUtils.buildOrder());
        when(mapper.map(Mockito.any(), Mockito.eq(Payment.class))).thenReturn(TestUtils.buildPayment());
        checkoutServiceImpl.createChekout(TestUtils.buildCheckoutDto());
        verify(repository, times(1)).save(Mockito.any());
    }

    @Test
    void testGetOrdersByConsumerWhenIdIsValid() {
        CheckoutConsumerDto dto = checkoutServiceImpl.getOrdersByConsumer(TestUtils.genUUID().toString());
        verify(service, times(1)).getOrdersByConsumer(Mockito.any(UUID.class));
        assertInstanceOf(CheckoutConsumerDto.class, dto);
    }

    @Test
    void testGetOrdersByConsumerWhenIdIsNotValid() {
        String msgError = assertThrows(CartConsumerException.class, () ->
                checkoutServiceImpl.getOrdersByConsumer("invalid-consumer-id")).getMessage();
        assertEquals("Invalid consumerId Format", msgError);
    }



}
