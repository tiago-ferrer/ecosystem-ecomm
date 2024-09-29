package br.com.fiap.postech.adjt.checkout.infrastructure.payment.client;

import br.com.fiap.postech.adjt.checkout.infrastructure.config.FeignClientFastConfig;
import br.com.fiap.postech.adjt.checkout.infrastructure.payment.client.request.PaymentRequestDTO;
import br.com.fiap.postech.adjt.checkout.infrastructure.payment.client.response.PaymentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "payment",
        url = "https://payment-api-latest.onrender.com",
        configuration = FeignClientFastConfig.class
)
public interface PaymentClient {

    @PostMapping("/create-payment")
    PaymentResponseDTO executa(@RequestBody final PaymentRequestDTO request,
                               @RequestHeader("apiKey") final String apiKey);

}
