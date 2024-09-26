package br.com.fiap.postech.adjt.cart.controller.impl;

import br.com.fiap.postech.adjt.cart.controller.CartController;
import br.com.fiap.postech.adjt.cart.model.dto.request.AddCartItemRequest;
import br.com.fiap.postech.adjt.cart.model.dto.request.ClearCartRequest;
import br.com.fiap.postech.adjt.cart.model.dto.request.FindCartByCustomerIdRequest;
import br.com.fiap.postech.adjt.cart.model.dto.request.IncrementCartItemRequest;
import br.com.fiap.postech.adjt.cart.model.dto.request.RemoveCartItemRequest;
import br.com.fiap.postech.adjt.cart.model.entity.Cart;
import br.com.fiap.postech.adjt.cart.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartControllerImpl implements CartController {
    private final CartService cartService;

    public CartControllerImpl(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    @PostMapping("/items")
    public ResponseEntity<Cart> add(
            @RequestBody AddCartItemRequest request) {

        Cart cart = cartService.add(request);

        return ResponseEntity.ok(cart);
    }

    @Override
    @DeleteMapping("/item")
    public ResponseEntity<Cart> remove(
            @RequestBody RemoveCartItemRequest request) {

        Cart cart = cartService.remove(request);

        return ResponseEntity.ok(cart);
    }

    @Override
    @PutMapping("/item")
    public ResponseEntity<Cart> increment(
            @RequestBody IncrementCartItemRequest request) {

        Cart cart = cartService.increment(request);

        return ResponseEntity.ok(cart);
    }

    @Override
    @GetMapping
    public ResponseEntity<Cart> findByCustomerId(
            @RequestBody FindCartByCustomerIdRequest request) {

        Cart cart = cartService.findByCustomerId(request.consumerId());

        return ResponseEntity.ok(cart);
    }

    @Override
    @DeleteMapping
    public ResponseEntity<Cart> clear(
            @RequestBody ClearCartRequest request) {

        Cart cart = cartService.clear(request.consumerId());

        return ResponseEntity.ok(cart);
    }
}
