package br.com.fiap.postech.adjt.checkout.consumer;

import br.com.fiap.postech.adjt.checkout.dto.order.OrderResponseDto;
import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentRequestDto;
import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentResponseDto;
import br.com.fiap.postech.adjt.checkout.entity.enums.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.entity.payment.PaymentConfirmed;
import br.com.fiap.postech.adjt.checkout.integracao.PaymentServiceAPI;
import br.com.fiap.postech.adjt.checkout.service.OrderService;
import br.com.fiap.postech.adjt.checkout.service.impl.PaymentConfirmedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Log4j2
@Component
@RequiredArgsConstructor
public class PaymentConsumer {

    private final PaymentServiceAPI paymentServiceAPI;
    private final OrderService orderService;
    private final PaymentConfirmedService paymentConfirmedService;

    @RabbitListener(queues = { "payment.queue" })
    public void receive(@Payload PaymentRequestDto paymentRequestDto) {

        log.info("Resposta da API externa recebida: " + paymentRequestDto.getOrderId());
        try {
            PaymentResponseDto paymentResponseDto = paymentServiceAPI.sendPayment(paymentRequestDto);

            OrderResponseDto orderDto = orderService.getById(paymentRequestDto.getOrderId());

            if (paymentResponseDto.getStatus().equalsIgnoreCase(PaymentStatus.approved.toString())) {
                orderService.updateStatusByStatusName(paymentRequestDto.getOrderId(), PaymentStatus.approved);
                orderDto.setPaymentStatus(PaymentStatus.approved);
                log.info("Pedido aprovado: " + paymentResponseDto);
            } else if (paymentResponseDto.getStatus().equalsIgnoreCase(PaymentStatus.declined.toString())) {
                orderService.updateStatusByStatusName(paymentRequestDto.getOrderId(), PaymentStatus.declined);
                orderDto.setPaymentStatus(PaymentStatus.declined);
                log.info("Pedido cancelado: " + paymentResponseDto);
            }

            PaymentConfirmed paymentConfirmed = new PaymentConfirmed(UUID.fromString(paymentResponseDto.getPaymentId()),
                    orderDto.getOrderId(), orderDto.getPaymentStatus());

            paymentConfirmedService.cadastrar(paymentConfirmed);
        } catch (Exception e) {
            orderService.updateStatusByStatusName(paymentRequestDto.getOrderId(), PaymentStatus.declined);
            log.error("Falha ao processar pagamento", e);
        }
    }

}
