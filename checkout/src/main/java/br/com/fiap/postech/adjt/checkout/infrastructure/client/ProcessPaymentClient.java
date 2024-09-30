package br.com.fiap.postech.adjt.checkout.infrastructure.client;

import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.PaymentRequest;
import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "process-payment", url = "https://payment-api-latest.onrender.com")
public interface ProcessPaymentClient  {

    @PostMapping("/create-payment")
    PaymentResponse exec(@RequestBody() PaymentRequest paymentRequest, @RequestHeader("Authorization") String token);
}
