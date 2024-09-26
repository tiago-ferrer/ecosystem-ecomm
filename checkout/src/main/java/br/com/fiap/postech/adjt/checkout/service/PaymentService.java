package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentService {
    private final RestTemplate restTemplate;

    @Value("${payment.api.url.createKey}")
    private String createApiKeyUrl;

    @Value("${payment.api.url.createPayment}")
    private String createPaymentUrl;

    public PaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String generateApiKey(ApiKeyRequestDTO apiKeyRequest) {
        ApiKeyResponseDTO response = restTemplate.postForObject(createApiKeyUrl, apiKeyRequest, ApiKeyResponseDTO.class);
        return response.apiKey();
    }

    public CheckoutResponseDTO processPayment(PaymentRequestDTO paymentRequest, String apiKey) {
        HttpHeaders headers = new HttpHeaders();
        System.out.println("HEADER "+headers);
        headers.set("apiKey", apiKey);
        HttpEntity<PaymentRequestDTO> requestEntity = new HttpEntity<>(paymentRequest, headers);
        System.out.println("requestEntity "+requestEntity);

        try {
            ResponseEntity<CheckoutResponseDTO> response = restTemplate.exchange(createPaymentUrl, HttpMethod.POST, requestEntity, CheckoutResponseDTO.class);
            System.out.println("response"+response);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Erro ao processar o pagamento: " + e.getResponseBodyAsString());
        }
    }
}

