package br.com.fiap.postech.adjt.payment.infrastructure.brokers;

import br.com.fiap.postech.adjt.payment.domain.useCases.ProcessPaymentUseCase;
import br.com.fiap.postech.adjt.payment.infrastructure.dtos.PaymentConsumerPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckoutEventListener implements Consumer<PaymentConsumerPayload> {
    private final ProcessPaymentUseCase processPaymentUseCase;
    @Override
    public void accept(PaymentConsumerPayload dto) {
        try {
            log.info("Mensagem recebida");
            processPaymentUseCase.exec(dto);
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }
}
