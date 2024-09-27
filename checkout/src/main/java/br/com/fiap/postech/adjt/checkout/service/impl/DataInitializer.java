package br.com.fiap.postech.adjt.checkout.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.fiap.postech.adjt.checkout.clients.PaymentClient;
import br.com.fiap.postech.adjt.checkout.model.OrderEntity;
import br.com.fiap.postech.adjt.checkout.model.dto.request.PaymentFieldsRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.request.PaymentMethodRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.request.PaymentRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.response.CheckoutResponse;
import br.com.fiap.postech.adjt.checkout.repository.OrderRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DataInitializer {

    @Value("${api.client.payment.key}")
    private String API_KEY;

    private final OrderRepository orderRepository;
    private final PaymentClient paymentClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public DataInitializer(OrderRepository orderRepository, PaymentClient paymentClient, ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.paymentClient = paymentClient;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedRate = 15000)
    public void init() {
        List<OrderEntity> pendingOrders = orderRepository.findByPaymentStatus("pending");

        for (OrderEntity order : pendingOrders) {
            
        	PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setOrderId(order.getOrderId().toString());
            paymentRequest.setAmount(order.getValue());
            paymentRequest.setCurrency("BRL");
            
            PaymentMethodRequest paymentMethod = new PaymentMethodRequest();
            paymentMethod.setType("br_credit_card");
            
            PaymentFieldsRequest fields = new PaymentFieldsRequest();
            fields.setNumber(order.getCard().getNumber());
            fields.setExpiration_month(order.getCard().getExpiration_month());
            fields.setExpiration_year(order.getCard().getExpiration_year());
            fields.setCvv(order.getCard().getCvv());
            fields.setName(order.getCard().getName());
            
            paymentMethod.setFields(fields);
            paymentRequest.setPayment_method(paymentMethod);

            try {
                String jsonPayload = objectMapper.writeValueAsString(paymentRequest);
                System.out.println("PaymentRequest Payload (JSON): " + jsonPayload);
            } catch (Exception e) {
                System.err.println("Failed to serialize PaymentRequest: " + e.getMessage());
            }

            try {
                CheckoutResponse paymentResponse = paymentClient.processPayment(API_KEY, paymentRequest);
                System.out.println("Payment response: " + paymentResponse.getStatus());
                order.setPaymentStatus(paymentResponse.getStatus());
                orderRepository.save(order);
                System.out.println("Order status updated to: " + order.getPaymentStatus());
            } catch (Exception e) {
                System.err.println("Payment failed: " + e.getMessage());
                order.setPaymentStatus("declined");
                orderRepository.save(order);
                System.out.println("Order status updated to: declined");
            }
        }
    }
}