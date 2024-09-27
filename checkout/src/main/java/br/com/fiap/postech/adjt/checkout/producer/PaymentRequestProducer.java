package br.com.fiap.postech.adjt.checkout.producer;

import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendPaymentRequest(PaymentRequestDto paymentRequestDto, String correlationId) {
        log.info("Enviando pagamento para fila RabbitMQ: " + paymentRequestDto.getOrderId());

        Message message = MessageBuilder.withBody(paymentRequestDto.toString().getBytes())
                .setCorrelationId(correlationId)
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .build();

        rabbitTemplate.convertAndSend("payment.requests", message);
    }
}
