package br.com.fiap.postech.adjt.checkout.controller;

import br.com.fiap.postech.adjt.checkout.controller.request.CreateOrderRequest;
import br.com.fiap.postech.adjt.checkout.controller.response.*;
import br.com.fiap.postech.adjt.checkout.entity.Order;
import br.com.fiap.postech.adjt.checkout.exception.InvalidConsumerId;
import br.com.fiap.postech.adjt.checkout.service.CheckoutService;
import br.com.fiap.postech.adjt.checkout.validator.UUIDValidator;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody @Valid CreateOrderRequest createOrderRequest) {
        try {
            if (UUIDValidator.isValidUUID(createOrderRequest.consumerId())) {
                Order order = checkoutService.createOrder(createOrderRequest);
                return ResponseEntity.ok().body(new CreateOrderResponse(order.getOrderId().toString(), order.getStatus().toString()));
            }
            throw new InvalidConsumerId();
        } catch (InvalidConsumerId e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @GetMapping("/{consumerId}")
    public ResponseEntity<?> findOrdersByConsumerId(@PathVariable("consumerId") String consumerId) {
        try {
            if (UUIDValidator.isValidUUID(consumerId)) {
                List<Order> orders = checkoutService.findOrdersByConsumerId(UUID.fromString(consumerId));
                List<OrderResponse> orderResponses = orders.stream().map(order -> new OrderResponse(
                            order.getOrderId().toString(),
                            order.getItems().stream().map(item -> new ItemResponse(item.getItemId(), item.getQnt())).toList(),
                            order.getPaymentType(),
                            order.getValue(),
                            order.getStatus()
                    )).toList();
                return ResponseEntity.ok().body(new GetOrdersByConsumerIdResponse(orderResponses));
            }
            throw new InvalidConsumerId();
        } catch (InvalidConsumerId e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @GetMapping("/by-order-id/{orderId}")
    public ResponseEntity<?> findOrderById(@PathVariable("orderId") String orderId) {
        try {
            if (UUIDValidator.isValidUUID(orderId)) {
                Order order = checkoutService.findOrderById(UUID.fromString(orderId));
                OrderResponse orderResponse = new OrderResponse(
                        order.getOrderId().toString(),
                        order.getItems().stream().map(item -> new ItemResponse(item.getItemId(), item.getQnt())).toList(),
                        order.getPaymentType(),
                        order.getValue(),
                        order.getStatus()
                );
                return ResponseEntity.ok().body(orderResponse);
            }
            throw new InvalidConsumerId();
        } catch (InvalidConsumerId e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e);
        }
    }

}
