package br.com.fiap.postech.adjt.checkout.producer;

import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentRequestDto;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class PaymentProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queuePaymentTest;

    @Async
    public void send(PaymentRequestDto paymentRequestDto) {
        this.rabbitTemplate.convertAndSend(queuePaymentTest.getName(), paymentRequestDto);
    }

}
