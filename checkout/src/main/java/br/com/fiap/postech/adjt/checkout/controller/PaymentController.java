package br.com.fiap.postech.adjt.checkout.controller;

import br.com.fiap.postech.adjt.checkout.dto.*;
import br.com.fiap.postech.adjt.checkout.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/generate-api-key")
    public ResponseEntity<ApiKeyResponseDTO> generateApiKey(@RequestBody ApiKeyRequestDTO apiKeyRequest) {
        String apiKey = paymentService.generateApiKey(apiKeyRequest);
        return ResponseEntity.ok(new ApiKeyResponseDTO(apiKey));
    }

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponseDTO> checkout(@RequestBody PaymentRequestDTO paymentRequest, @RequestHeader("apiKey") String apiKey) {
        CheckoutResponseDTO response = paymentService.processPayment(paymentRequest, apiKey);
        return ResponseEntity.ok(response);
    }
}
