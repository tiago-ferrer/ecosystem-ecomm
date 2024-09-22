package br.com.fiap.postech.adjt.cart.controller;

import br.com.fiap.postech.adjt.cart.dto.CartConsumerRequest;
import br.com.fiap.postech.adjt.cart.dto.CartItemRequest;
import br.com.fiap.postech.adjt.cart.dto.ResponseMessage;
import br.com.fiap.postech.adjt.cart.model.Cart;
import br.com.fiap.postech.adjt.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<?> addItem(@RequestBody CartItemRequest request) {
        String message = cartService.addItem(request.getConsumerId(), request.getItemId(), request.getQuantity());
        return ResponseEntity.ok(new ResponseMessage(message));
    }

    @DeleteMapping("/item")
    public ResponseEntity<?> removeItem(@RequestBody CartItemRequest request) {
        String message = cartService.removeItem(request.getConsumerId(), request.getItemId());
        return ResponseEntity.ok(new ResponseMessage(message));
    }

    @PutMapping("/item")
    public ResponseEntity<?> incrementItem(@RequestBody CartItemRequest request) {
        String message = cartService.incrementItem(request.getConsumerId(), request.getItemId());
        return ResponseEntity.ok(new ResponseMessage(message));
    }

    @GetMapping("/")
    public ResponseEntity<?> getCart(@RequestBody CartConsumerRequest request) {
        Cart cart = cartService.getCart(request.getConsumerId());
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/")
    public ResponseEntity<?> clearCart(@RequestBody CartConsumerRequest request) {
        String message = cartService.clearCart(request.getConsumerId());
        return ResponseEntity.ok(new ResponseMessage(message));
    }
}
