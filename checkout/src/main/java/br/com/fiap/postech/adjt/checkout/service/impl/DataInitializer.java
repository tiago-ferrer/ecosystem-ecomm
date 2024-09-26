package br.com.fiap.postech.adjt.checkout.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.fiap.postech.adjt.checkout.clients.PaymentClient;
import br.com.fiap.postech.adjt.checkout.model.OrderEntity;
import br.com.fiap.postech.adjt.checkout.model.request.PaymentFieldsRequest;
import br.com.fiap.postech.adjt.checkout.model.request.PaymentMethodRequest;
import br.com.fiap.postech.adjt.checkout.model.request.PaymentRequest;
import br.com.fiap.postech.adjt.checkout.model.response.CheckoutResponse;
import br.com.fiap.postech.adjt.checkout.repository.OrderRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DataInitializer {

    private final OrderRepository orderRepository;
    private final PaymentClient paymentClient;
    private final ObjectMapper objectMapper; // Adiciona o ObjectMapper

    @Autowired
    public DataInitializer(OrderRepository orderRepository, PaymentClient paymentClient, ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.paymentClient = paymentClient;
        this.objectMapper = objectMapper; // Inicializa o ObjectMapper
    }

    // Executa a cada 1 minuto (60.000 milissegundos)
    @Scheduled(fixedRate = 60000)
    public void init() {
        List<OrderEntity> pendingOrders = orderRepository.findByPaymentStatus("pending");
        System.out.println("Pending orders: " + pendingOrders.size()); // Log para verificar pedidos pendentes

        for (OrderEntity order : pendingOrders) {
            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setOrderId(order.getOrderId().toString()); // Corrigido para usar o orderId
            paymentRequest.setAmount(order.getValue());
            paymentRequest.setCurrency("BRL");

            // Monta o PaymentMethodRequest corretamente
            PaymentMethodRequest paymentMethod = new PaymentMethodRequest();
            paymentMethod.setType("br_credit_card");
            PaymentFieldsRequest fields = new PaymentFieldsRequest();
            fields.setNumber("4111111111111111");
            fields.setExpiration_month("12");
            fields.setExpiration_year("25");
            fields.setCvv("789");
            fields.setName("John Doe");
            paymentMethod.setFields(fields);
            paymentRequest.setPayment_method(paymentMethod);

            // Serializa o PaymentRequest para JSON e imprime
            try {
                String jsonPayload = objectMapper.writeValueAsString(paymentRequest);
                System.out.println("PaymentRequest Payload (JSON): " + jsonPayload);
            } catch (Exception e) {
                System.err.println("Failed to serialize PaymentRequest: " + e.getMessage());
            }

            try {
                CheckoutResponse paymentResponse = paymentClient.processPayment("9f6ce8f2761d1a9a42b722045cc712785f444455e726582d947c14aa313c2fa3", paymentRequest);
                System.out.println("Payment response: " + paymentResponse.getStatus()); // Log da resposta de pagamento
                order.setPaymentStatus(paymentResponse.getStatus());
                orderRepository.save(order);
                System.out.println("Order status updated to: " + order.getPaymentStatus()); // Log da atualização do status
            } catch (Exception e) {
                System.err.println("Payment failed: " + e.getMessage()); // Log do erro
                order.setPaymentStatus("declined");
                orderRepository.save(order);
                System.out.println("Order status updated to: declined"); // Log da atualização do status
            }
        }
    }
}