package br.com.fiap.postech.adjt.checkout.controller;

import br.com.fiap.postech.adjt.checkout.dto.CheckoutRequestDTO;
import br.com.fiap.postech.adjt.checkout.dto.CheckoutResponseDTO;
import br.com.fiap.postech.adjt.checkout.dto.OrderItemDTO;
import br.com.fiap.postech.adjt.checkout.dto.OrderResponseDTO;
import br.com.fiap.postech.adjt.checkout.model.Order;
import br.com.fiap.postech.adjt.checkout.service.CheckoutService;
import br.com.fiap.postech.adjt.checkout.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<CheckoutResponseDTO> processCheckout(@RequestBody CheckoutRequestDTO checkoutRequest) {
        try {
            CheckoutResponseDTO response = checkoutService.processCheckout(checkoutRequest);
            System.out.println("RESPONSE "+response);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException"+e);
            return ResponseEntity.badRequest().body(new CheckoutResponseDTO(null, "Invalid orderId format"));
        } catch (Exception e) {
            System.out.println("Exception"+e);
            return ResponseEntity.badRequest().body(new CheckoutResponseDTO(null, "FAILED"));
        }
    }

    @GetMapping("/{consumerId}")
    public ResponseEntity<List<Order>> getOrders(@PathVariable UUID consumerId) {
        List<Order> orders = orderService.getOrdersByConsumerId(consumerId);
        System.out.println("Pedidos encontrados para o consumerId " + consumerId + ": " + orders.size());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/by-order-id/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable UUID orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            OrderResponseDTO response = createOrderResponseDTO(order);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    private OrderResponseDTO createOrderResponseDTO(Order order) {
        List<OrderItemDTO> orderItems = order.getItems().stream()
                .map(item -> new OrderItemDTO(order.getConsumerId(), item.getItemId(), item.getQnt()))
                .toList();

        return new OrderResponseDTO(
                order.getId(),
                orderItems,
                order.getPaymentType(),
                order.getValue(),
                order.getPaymentStatus()
        );
    }

}








