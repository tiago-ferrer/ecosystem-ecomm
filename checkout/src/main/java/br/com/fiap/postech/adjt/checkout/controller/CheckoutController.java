package br.com.fiap.postech.adjt.checkout.controller;

import br.com.fiap.postech.adjt.checkout.model.OrderEntity;
import br.com.fiap.postech.adjt.checkout.model.request.CheckoutRequest;
import br.com.fiap.postech.adjt.checkout.model.response.CheckoutResponse;
import br.com.fiap.postech.adjt.checkout.model.response.OrderCheckoutsResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

public interface CheckoutController {

    ResponseEntity<CheckoutResponse> createCheckout(@Valid @RequestBody CheckoutRequest checkoutRequest);

    ResponseEntity<List<OrderCheckoutsResponse>> getOrdersByConsumerId(@PathVariable UUID consumerId);

    ResponseEntity<OrderEntity> getOrderById(@PathVariable UUID orderId);
}