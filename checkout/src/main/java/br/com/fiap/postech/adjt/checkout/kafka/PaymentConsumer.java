package br.com.fiap.postech.adjt.checkout.kafka;

import br.com.fiap.postech.adjt.checkout.dto.CheckoutResponseDTO;
import br.com.fiap.postech.adjt.checkout.dto.ExternalPaymentRequestDTO;
import br.com.fiap.postech.adjt.checkout.dto.ExternalPaymentResponseDTO;
import br.com.fiap.postech.adjt.checkout.model.Checkout;
import br.com.fiap.postech.adjt.checkout.model.Order;
import br.com.fiap.postech.adjt.checkout.model.PaymentMessage;
import br.com.fiap.postech.adjt.checkout.model.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.service.CartService;
import br.com.fiap.postech.adjt.checkout.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentConsumer {

    private static final Logger log = LoggerFactory.getLogger(PaymentConsumer.class);
    private final RestTemplate restTemplate;
    private final OrderService orderService;
    private final String paymentServiceUrl;
    private final CartService cartService;

    @Value("${api.key.value}")
    private String apiKeyValue;

    public PaymentConsumer(RestTemplate restTemplate, OrderService orderService, @Value("${payment.service.url}") String paymentServiceUrl, CartService cartService) {
        this.restTemplate = restTemplate;
        this.orderService = orderService;
        this.paymentServiceUrl = paymentServiceUrl;
        this.cartService = cartService;
    }

    @KafkaListener(topics = "payment-topic", groupId = "payment-group")
    public void consumePayment(PaymentMessage paymentMessage) {
        Order order = paymentMessage.getOrder();
        Checkout checkout = paymentMessage.getCheckout();
        ExternalPaymentRequestDTO externalPayment = createExternalPayment(order, checkout);

        HttpHeaders headers = new HttpHeaders();
        headers.add("apiKey", apiKeyValue);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ExternalPaymentRequestDTO> requestEntity = new HttpEntity<>(externalPayment, headers);

        ResponseEntity<ExternalPaymentResponseDTO> response = restTemplate.postForEntity(
                paymentServiceUrl,
                requestEntity,
                ExternalPaymentResponseDTO.class
        );
        if (response.getBody() != null && response.getBody().status().equals(PaymentStatus.approved.toString())) {
            order.setPaymentStatus(PaymentStatus.approved);
            cartService.clearCart(checkout.getConsumerId());
        } else {
            order.setPaymentStatus(PaymentStatus.declined);
        }

        orderService.updateOrder(order);
    }

    private ExternalPaymentRequestDTO createExternalPayment(Order order, Checkout checkout) {
        ExternalPaymentRequestDTO externalPayment = new ExternalPaymentRequestDTO(order.getOrderId().toString(), order.getValue(), order.getCurrency().toString(), checkout.getPaymentMethod());
        return externalPayment;
    }
}
