package br.com.fiap.postech.adjt.checkout.infrastructure.brokers;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class MessageProperties {
    private String checkoutChannel = "checkoutSupplier-out-0";
}
