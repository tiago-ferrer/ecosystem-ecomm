package br.com.fiap.postech.adjt.checkout.service.impl;

import br.com.fiap.postech.adjt.checkout.model.Order;
import br.com.fiap.postech.adjt.checkout.model.dto.request.PaymentFieldsRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.request.PaymentMethodRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.request.PaymentRequest;
import br.com.fiap.postech.adjt.checkout.repository.OrderRepository;
import br.com.fiap.postech.adjt.checkout.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataInitializer {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;

    public DataInitializer(
            ObjectMapper objectMapper,
            OrderRepository orderRepository,
            PaymentService paymentService
    ) {
        this.objectMapper = objectMapper;
        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
    }

    @Scheduled(fixedRate = 15000)
    public void init() {
        List<Order> pendingOrders = orderRepository.findByPaymentStatus("pending");

        for (Order order : pendingOrders) {
            PaymentRequest paymentRequest = getPaymentRequest(order);

            try {
                String jsonPayload = objectMapper.writeValueAsString(paymentRequest);
                logger.info("PaymentRequest Payload (JSON): {}", jsonPayload);
            } catch (Exception e) {
                logger.error("Failed to serialize PaymentRequest: {}", e.getMessage());
            }

            paymentService.process(order, paymentRequest);
        }
    }

    private PaymentRequest getPaymentRequest(Order order) {
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
        return paymentRequest;
    }
}