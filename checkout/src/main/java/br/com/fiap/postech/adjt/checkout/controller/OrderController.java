package br.com.fiap.postech.adjt.checkout.controller;


import br.com.fiap.postech.adjt.checkout.dto.order.OrderRequestDto;
import br.com.fiap.postech.adjt.checkout.dto.order.OrderResponseDto;
import br.com.fiap.postech.adjt.checkout.entity.order.Order;
import br.com.fiap.postech.adjt.checkout.exception.InvalidOrderIdException;
import br.com.fiap.postech.adjt.checkout.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/by-order-id")
public class OrderController {
    @Autowired
    private OrderService service;

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getByOderId(@PathVariable String orderId){
        try {
            OrderResponseDto order = service.retrieveById(UUID.fromString(orderId));
            return ResponseEntity.ok(order);
        }
        catch (RuntimeException e){
            throw new InvalidOrderIdException("Invalid orderId format");
        }
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDto orderDto){
        try {
            Order order = service.createOrder(orderDto);
            return ResponseEntity.ok(order);
        }
        catch (RuntimeException e){
            return ResponseEntity.badRequest().body(new InvalidOrderIdException("Invalid orderId format"));
        }
    }
}
