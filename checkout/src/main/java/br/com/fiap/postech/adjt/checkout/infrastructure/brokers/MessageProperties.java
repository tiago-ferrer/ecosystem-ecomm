package br.com.fiap.postech.adjt.checkout.infrastructure.brokers.config;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class MessageProperties {
    private String clienteAcceptedChannel = "checkoutSupplier-out-0";
}
