package br.com.fiap.postech.adjt.checkout.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.fiap.postech.adjt.checkout.clients.PaymentClient;
import br.com.fiap.postech.adjt.checkout.model.OrderEntity;
import br.com.fiap.postech.adjt.checkout.model.request.PaymentRequest;
import br.com.fiap.postech.adjt.checkout.model.response.CheckoutResponse;
import br.com.fiap.postech.adjt.checkout.repository.OrderRepository;

@Service
public class DataInitializer {

    private final OrderRepository orderRepository;
    private final PaymentClient paymentClient;

    @Autowired
    public DataInitializer(OrderRepository orderRepository, PaymentClient paymentClient) {
        this.orderRepository = orderRepository;
        this.paymentClient = paymentClient;
    }

    // Executa a cada 1 minuto (60.000 milissegundos)
    @Scheduled(fixedRate = 60000)
    public void init() {
        List<OrderEntity> pendingOrders = orderRepository.findByPaymentStatus("pending");
        System.out.println("Pending orders: " + pendingOrders.size()); // Log para verificar pedidos pendentes

        for (OrderEntity order : pendingOrders) {
            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setConsumerId(order.getConsumerId().toString());
            paymentRequest.setAmount(order.getValue());
            paymentRequest.setCurrency("BRL");

            try {
                CheckoutResponse paymentResponse = paymentClient.processPayment("apiKey", paymentRequest);
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