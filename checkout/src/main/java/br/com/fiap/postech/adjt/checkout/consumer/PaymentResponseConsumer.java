package br.com.fiap.postech.adjt.checkout.consumer;

import br.com.fiap.postech.adjt.checkout.dto.order.OrderResponseDto;
import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentRequestDto;
import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentResponseDto;
import br.com.fiap.postech.adjt.checkout.entity.enums.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.entity.payment.PaymentConfirmed;
import br.com.fiap.postech.adjt.checkout.exception.InvalidOrderIdException;
import br.com.fiap.postech.adjt.checkout.integracao.PaymentServiceAPI;
import br.com.fiap.postech.adjt.checkout.service.OrderService;
import br.com.fiap.postech.adjt.checkout.service.impl.PaymentConfirmedService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@AllArgsConstructor
public class PaymentResponseConsumer {

    private final PaymentConfirmedService paymentConfirmedService;
    private final OrderService service;
    private final PaymentServiceAPI paymentServiceAPI;

    @RabbitListener(queues = "payment.responses")
    public void consumePaymentResponse(PaymentResponseDto paymentResponseDto, Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info("Resposta de pagamento recebida com CorrelationId: " + correlationId);

        if (paymentResponseDto.getStatus().equalsIgnoreCase("APPROVED")) {
            log.info("Pagamento aprovado para o pedido: " + paymentResponseDto.getPaymentId());

        } else if (paymentResponseDto.getStatus().equalsIgnoreCase("DECLINED")) {
            log.info("Pagamento recusado para o pedido: " + paymentResponseDto.getPaymentId());
        } else {
            log.warn("Status de pagamento desconhecido para o pedido: " + paymentResponseDto.getPaymentId());
        }
    }


}
