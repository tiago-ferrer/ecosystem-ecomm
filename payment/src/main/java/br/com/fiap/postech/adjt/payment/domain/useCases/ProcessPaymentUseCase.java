package br.com.fiap.postech.adjt.payment.domain.useCases;

import br.com.fiap.postech.adjt.payment.infrastructure.client.GetCartClient;
import br.com.fiap.postech.adjt.payment.infrastructure.dtos.*;
import br.com.fiap.postech.adjt.payment.infrastructure.persistance.gateways.OrderGateway;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class ProcessPaymentUseCase {

    private final GetCartClient getCartList;
    private final PaymentRequestUseCase paymentRequestUseCase;
    private final OrderGateway orderGateway;

    public void exec(PaymentConsumerPayload dto) throws BadRequestException {
        log.info("Aqui passou");
        UUID id = UUID.fromString(dto.orderId());
        PaymentRequest paymentRequest = orderGateway.findById(id);
        log.info("Encontrou a ordem");
        PaymentResponse paymentResponse = paymentRequestUseCase.exec(paymentRequest);
        log.info("Fez a requisição");
        orderGateway.updateOrderStatus(id, paymentResponse.status());
        log.info("O pagamento foi processado!!!! " + paymentResponse.status());
    }
}
