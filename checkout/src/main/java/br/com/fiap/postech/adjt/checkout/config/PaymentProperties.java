package br.com.fiap.postech.adjt.checkout.config;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class PaymentProperties {
    private String paymentCreatedChannel = "paymentCreatedEventListner-in-0";

}
