package br.com.fiap.postech.adjt.checkout.controller.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.postech.adjt.checkout.controller.CheckoutController;
import br.com.fiap.postech.adjt.checkout.model.OrderEntity;
import br.com.fiap.postech.adjt.checkout.model.request.CheckoutRequest;
import br.com.fiap.postech.adjt.checkout.model.response.PaymentResponse;
import br.com.fiap.postech.adjt.checkout.service.impl.CheckoutServiceImpl;

@RestController
public class CheckoutControllerImpl implements CheckoutController {

    private final CheckoutServiceImpl checkoutService;

    @Autowired
    public CheckoutControllerImpl(CheckoutServiceImpl checkoutService) {
        this.checkoutService = checkoutService;
    }

    @Override
    public ResponseEntity<PaymentResponse> createCheckout(CheckoutRequest checkoutRequest) {
        checkoutRequest.validateConsumerId();

        PaymentResponse paymentResponse = checkoutService.processCheckout(
                checkoutRequest.getConsumerId(),
                checkoutRequest.getAmount(),
                checkoutRequest.getCurrency(),
                checkoutRequest.getPaymentMethod()
        );

        return ResponseEntity.ok(paymentResponse);
    }

    @Override
    public ResponseEntity<OrderEntity> getOrderById(UUID orderId) {
        OrderEntity order = checkoutService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }
    
    @Override
    public ResponseEntity<List<OrderEntity>> getOrdersByConsumerId(UUID consumerId) {
        List<OrderEntity> orders = checkoutService.getOrdersByConsumerId(consumerId);
        return ResponseEntity.ok(orders);
    }
 
}