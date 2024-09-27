package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.model.Checkout;
import br.com.fiap.postech.adjt.checkout.model.Order;
import br.com.fiap.postech.adjt.checkout.model.PaymentMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class PaymentProducer {

    private static final String TOPIC = "payment-topic";
    private final KafkaTemplate<String, PaymentMessage> kafkaTemplate;

    public PaymentProducer(KafkaTemplate<String, PaymentMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Async
    public void sendPaymentRequest(Order order, Checkout checkout) {
        PaymentMessage paymentMessage = new PaymentMessage();
        paymentMessage.setOrder(order);
        paymentMessage.setCheckout(checkout);

        kafkaTemplate.send(TOPIC, paymentMessage);
    }
}
