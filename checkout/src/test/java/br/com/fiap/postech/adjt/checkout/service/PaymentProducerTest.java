package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.kafka.PaymentProducer;
import br.com.fiap.postech.adjt.checkout.model.Checkout;
import br.com.fiap.postech.adjt.checkout.model.Order;
import br.com.fiap.postech.adjt.checkout.model.PaymentMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class PaymentProducerTest {

    @Mock
    private KafkaTemplate<String, PaymentMessage> kafkaTemplate;

    @InjectMocks
    private PaymentProducer paymentProducer;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendPaymentRequest_shouldSendPaymentMessageToKafka() {

        Order order = new Order();
        order.setValue(100);

        Checkout checkout = new Checkout();
        checkout.setConsumerId(UUID.randomUUID());

        paymentProducer.sendPaymentRequest(order, checkout);

        verify(kafkaTemplate).send(eq("payment-topic"), any(PaymentMessage.class));
    }
}