package br.com.fiap.postech.adjt.checkout.service.impl;

import br.com.fiap.postech.adjt.checkout.clients.PaymentClient;
import br.com.fiap.postech.adjt.checkout.model.Order;
import br.com.fiap.postech.adjt.checkout.model.dto.request.PaymentRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.response.CheckoutResponse;
import br.com.fiap.postech.adjt.checkout.repository.OrderRepository;
import br.com.fiap.postech.adjt.checkout.service.PaymentService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${api.client.payment.key}")
    private String API_KEY;

    private final PaymentClient paymentClient;
    private final OrderRepository orderRepository;

    public PaymentServiceImpl(PaymentClient paymentClient, OrderRepository orderRepository) {
        this.paymentClient = paymentClient;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public void process(Order order, PaymentRequest paymentRequest) {
        try {
            CheckoutResponse paymentResponse = paymentClient.processPayment(API_KEY, paymentRequest);
            order.setPaymentStatus(paymentResponse.getStatus());
            logger.info("Payment processing result in order {}: {}", order.getOrderId(), paymentResponse.getStatus());
        } catch (Exception e) {
            logger.info("Payment processing result in order {}: declined", order.getOrderId());
            logger.error("error: ", e.getMessage());

            order.setPaymentStatus("declined");
            logger.info("Order {} status updated to: declined", order.getOrderId());
        } finally {
            orderRepository.save(order);
        }
    }
}
