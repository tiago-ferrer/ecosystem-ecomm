package br.com.fiap.postech.adjt.cart.controller;

import br.com.fiap.postech.adjt.cart.dto.AddItemToCartDTO;
import br.com.fiap.postech.adjt.cart.model.Cart;
import br.com.fiap.postech.adjt.cart.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addItemToCart(
            @RequestParam UUID consumerId,
            @RequestBody AddItemToCartDTO addItemToCartDTO) {
        return cartService.addItemToCart(consumerId, addItemToCartDTO);
    }


    @GetMapping("/items")
    public ResponseEntity<List<Cart>> getCartItems(@RequestParam UUID consumerId) {
        List<Cart> items = cartService.getCartItems(consumerId);
        return ResponseEntity.ok(items);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeItemFromCart(
            @RequestParam String consumerId,
            @RequestParam Long itemId) {
        return cartService.removeItemFromCart(consumerId, itemId);
    }


    @DeleteMapping("/removeAll")
    public ResponseEntity<?> removeAllItemsFromCart(@RequestParam String consumerId) {
        return cartService.removeAllItemsFromCart(consumerId);
    }


}
