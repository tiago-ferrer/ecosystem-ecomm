package br.com.fiap.postech.adjt.checkout.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.postech.adjt.checkout.clients.PaymentClient;
import br.com.fiap.postech.adjt.checkout.model.OrderEntity;
import br.com.fiap.postech.adjt.checkout.model.request.PaymentRequest;
import br.com.fiap.postech.adjt.checkout.model.response.PaymentResponse;
import br.com.fiap.postech.adjt.checkout.repository.OrderRepository;
import jakarta.annotation.PostConstruct;

@Service
public class DataInitializer {

    private final OrderRepository orderRepository;
    private final PaymentClient paymentClient;

    @Autowired
    public DataInitializer(OrderRepository orderRepository, PaymentClient paymentClient) {
        this.orderRepository = orderRepository;
        this.paymentClient = paymentClient;
    }

    @PostConstruct
    public void init() {
        List<OrderEntity> pendingOrders = orderRepository.findByPaymentStatus("pending");
        for (OrderEntity order : pendingOrders) {
            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setOrderId(order.getOrderId().toString());
            paymentRequest.setAmount(order.getValue());
            paymentRequest.setCurrency("BRL");

            try {
                PaymentResponse paymentResponse = paymentClient.processPayment("apiKey", paymentRequest);
                order.setPaymentStatus(paymentResponse.getStatus());
                orderRepository.save(order);
            } catch (Exception e) {
                order.setPaymentStatus("declined");
                orderRepository.save(order);
            }
        }
    }
}