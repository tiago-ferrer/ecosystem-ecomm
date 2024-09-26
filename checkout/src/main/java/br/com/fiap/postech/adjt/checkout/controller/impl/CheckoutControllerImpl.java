package br.com.fiap.postech.adjt.checkout.controller.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.postech.adjt.checkout.controller.CheckoutController;
import br.com.fiap.postech.adjt.checkout.model.OrderEntity;
import br.com.fiap.postech.adjt.checkout.model.request.CheckoutRequest;
import br.com.fiap.postech.adjt.checkout.model.response.CheckoutResponse;
import br.com.fiap.postech.adjt.checkout.model.response.OrderCheckoutsResponse;
import br.com.fiap.postech.adjt.checkout.service.CheckoutService;

@RestController
public class CheckoutControllerImpl implements CheckoutController {

    private final CheckoutService checkoutService;

    @Autowired
    public CheckoutControllerImpl(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @Override
    public ResponseEntity<CheckoutResponse> createCheckout(CheckoutRequest checkoutRequest) {
        checkoutRequest.validateConsumerId();

        CheckoutResponse checkoutResponse = checkoutService.processCheckout(
        		UUID.fromString(checkoutRequest.getConsumerId()),
        		checkoutRequest.getAmount(),
        		checkoutRequest.getCurrency(),
        		checkoutRequest.getPaymentMethod()
        );
                return ResponseEntity.ok(checkoutResponse);
    }

    @Override
    public ResponseEntity<List<OrderCheckoutsResponse>> getOrdersByConsumerId(UUID consumerId) {
        List<OrderCheckoutsResponse> orders = checkoutService.getOrdersByConsumerId(consumerId);
        return ResponseEntity.ok(orders);
    }
    
    @Override
    public ResponseEntity<OrderEntity> getOrderById(UUID orderId) {
        OrderEntity order = checkoutService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }
    
}