package br.com.fiap.postech.adjt.checkout.controller;

import br.com.fiap.postech.adjt.checkout.model.Order;
import br.com.fiap.postech.adjt.checkout.model.dto.request.CheckoutRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.response.CheckoutResponse;
import br.com.fiap.postech.adjt.checkout.model.dto.response.OrderCheckoutsResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

public interface CheckoutController {

    ResponseEntity<CheckoutResponse> createCheckout(@Valid @RequestBody CheckoutRequest checkoutRequest);

    ResponseEntity<List<OrderCheckoutsResponse>> getOrdersByConsumerId(@PathVariable UUID consumerId);

    ResponseEntity<Order> getOrderById(@PathVariable UUID orderId);
}