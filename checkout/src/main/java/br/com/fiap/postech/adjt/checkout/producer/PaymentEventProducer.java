package br.com.fiap.postech.adjt.checkout.producer;


import br.com.fiap.postech.adjt.checkout.config.PaymentProperties;
import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventProducer implements PaymentEvent{

    private final StreamBridge streamBridge;
    private final PaymentProperties paymentProperties;

    @Override
    public void sendPaymentCreateEvent(PaymentRequestDto payment) {
        log.info("Pagamento alterado " + payment.getOrderId());
        streamBridge.send(paymentProperties.getPaymentCreatedChannel(), payment);
    }
}
