package br.com.fiap.postech.adjt.cart.controller;

import br.com.fiap.postech.adjt.cart.domain.Cart;
import br.com.fiap.postech.adjt.cart.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.fiap.postech.adjt.cart.service.CartService;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{consumerId}/add")
    public ResponseEntity<Cart> addItem(@PathVariable UUID consumerId, @RequestBody Item item) {
        Cart updatedCart = cartService.addItem(consumerId, item);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{consumerId}/remove/{itemId}")
    public ResponseEntity<Cart> removeItem(@PathVariable UUID consumerId, @PathVariable UUID itemId) {
        Cart updatedCart = cartService.removeItem(consumerId, itemId);
        return ResponseEntity.ok(updatedCart);
    }

    @PatchMapping("/{consumerId}/increment/{itemId}")
    public ResponseEntity<Cart> incrementItem(@PathVariable UUID consumerId, @PathVariable UUID itemId, @RequestParam int quantity) {
        Cart updatedCart = cartService.incrementItem(consumerId, itemId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{consumerId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable UUID consumerId) {
        cartService.clearCart(consumerId);
        return ResponseEntity.noContent().build();
    }
}
