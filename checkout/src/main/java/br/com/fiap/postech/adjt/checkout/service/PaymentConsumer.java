package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.dto.CheckoutResponseDTO;
import br.com.fiap.postech.adjt.checkout.dto.ExternalPaymentRequestDTO;
import br.com.fiap.postech.adjt.checkout.model.Checkout;
import br.com.fiap.postech.adjt.checkout.model.Order;
import br.com.fiap.postech.adjt.checkout.model.PaymentMessage;
import br.com.fiap.postech.adjt.checkout.model.PaymentStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentConsumer {

    private final RestTemplate restTemplate;
    private final OrderService orderService;
    private final String paymentServiceUrl;
    private final CartService cartService;

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

        // Enviar requisição de pagamento para a API externa
        ResponseEntity<CheckoutResponseDTO> response = restTemplate.postForEntity(
                paymentServiceUrl,
                externalPayment,
                CheckoutResponseDTO.class
        );

        // Atualizar o status do pedido baseado na resposta da payment-api
        if (response.getBody() != null && response.getBody().paymentStatus().equals(PaymentStatus.approved)) {
            order.setPaymentStatus(PaymentStatus.approved);
            cartService.clearCart(checkout.getConsumerId());
        } else {
            order.setPaymentStatus(PaymentStatus.declined);
        }

        // Persistir a atualização do pedido no banco
        orderService.updateOrder(order);
    }

    private ExternalPaymentRequestDTO createExternalPayment(Order order, Checkout checkout) {
        ExternalPaymentRequestDTO externalPayment = new ExternalPaymentRequestDTO(order.getOrderId().toString(),order.getTotalValue(),order.getCurrency().toString(),checkout.getPaymentMethod());
        return externalPayment;
    }
}
