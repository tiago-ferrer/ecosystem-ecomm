package br.com.fiap.postech.adjt.checkout.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import br.com.fiap.postech.adjt.checkout.model.request.PaymentRequest;
import br.com.fiap.postech.adjt.checkout.model.response.CheckoutResponse;

@FeignClient(name = "paymentClient", url = "${api.client.payment.url}")
public interface PaymentClient {
    @PostMapping("/create-payment")
    CheckoutResponse processPayment(
    		@RequestHeader("apiKey") String apiKey, 
    		@RequestBody PaymentRequest paymentRequest);
}