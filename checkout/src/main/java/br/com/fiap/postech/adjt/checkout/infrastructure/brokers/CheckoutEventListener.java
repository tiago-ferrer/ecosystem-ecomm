package br.com.fiap.postech.adjt.checkout.infrastructure.brokers;
import br.com.fiap.postech.adjt.checkout.domain.entities.useCases.PaymentRequestUseCase;
import br.com.fiap.postech.adjt.checkout.infrastructure.client.DeleteCartClient;
import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.GetCartPayload;
import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.PaymentConsumerPayload;
import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.PaymentRequest;
import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.PaymentResponse;
import br.com.fiap.postech.adjt.checkout.infrastructure.persistance.gateways.OrderGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckoutEventListener implements Consumer<PaymentConsumerPayload> {

    private final OrderGateway orderGateway;
    private final PaymentRequestUseCase paymentRequestUseCase;
    private final DeleteCartClient deleteCartClient;

    @Override
    public void accept(PaymentConsumerPayload dto) {
        log.info("Mensagem recebida");
        UUID id = dto.orderId();
        PaymentRequest paymentRequest = orderGateway.findById(id);
        log.info("Encontrou a ordem");
        PaymentResponse paymentResponse = paymentRequestUseCase.exec(paymentRequest);
        log.info("Fez a requisição");
        orderGateway.updateOrderStatus(id, paymentResponse.status());
        log.info("Agora temos o status " + paymentResponse.status());
        deleteCartClient.exec(new GetCartPayload(dto.consumerId()));

    }
}