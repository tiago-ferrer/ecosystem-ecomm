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
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/items")
    public ResponseEntity<?> addItemToCart(
            @RequestParam UUID consumerId,
            @RequestBody AddItemToCartDTO addItemToCartDTO) {
        return cartService.addItemToCart(consumerId, addItemToCartDTO);
    }

    @GetMapping
    public ResponseEntity<List<Cart>> getCartItems(@RequestParam UUID consumerId) {
        List<Cart> items = cartService.getCartItems(consumerId);
        return ResponseEntity.ok(items);
    }

    @DeleteMapping("/item")
    public ResponseEntity<?> removeItemFromCart(
            @RequestParam String consumerId,
            @RequestParam Long itemId) {
        return cartService.removeItemFromCart(consumerId, itemId);
    }


    @DeleteMapping
    public ResponseEntity<?> removeAllItemsFromCart(@RequestParam String consumerId) {
        return cartService.removeAllItemsFromCart(consumerId);
    }


}
