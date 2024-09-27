package br.com.fiap.postech.adjt.checkout.consumer;

import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentRequestDto;
import br.com.fiap.postech.adjt.checkout.integracao.PaymentServiceAPI;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class PaymentRequestConsumer {

    private final PaymentServiceAPI paymentServiceAPI;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "payment.requests")
    public void consumePaymentRequest(PaymentRequestDto paymentRequestDto, Message message) {
        log.info("Recebendo solicitação de pagamento: " + paymentRequestDto.getOrderId());

        String correlationId = message.getMessageProperties().getCorrelationId();

        paymentServiceAPI.processPayment(paymentRequestDto)
                .subscribe(paymentResponseDto -> {
                    Message responseMessage = MessageBuilder.withBody(paymentResponseDto.toString().getBytes())
                            .setCorrelationId(correlationId)
                            .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                            .build();

                    rabbitTemplate.convertAndSend("payment.responses", responseMessage);
                }, error -> log.error("Erro ao processar pagamento: " + error.getMessage()));
    }
}
