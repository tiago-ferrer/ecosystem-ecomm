package br.com.fiap.postech.adjt.checkout.infrastructure.brokers.config;


import br.com.fiap.postech.adjt.checkout.infrastructure.gateways.CheckoutEventGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckoutWithStreamBridge implements CheckoutEventGateway {
    private final StreamBridge streamBridge;
    private final MessageProperties messageProperties;
    @Override
    public void sendClienteAcceptedEvent() {
        log.info("Mensagem enviada para o serviço de produtos");
        streamBridge.send(messageProperties.getClienteAcceptedChannel(), pedidoCreated);
    }
}