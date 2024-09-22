package br.com.fiap.postech.adjt.cart.controller.impl;

import br.com.fiap.postech.adjt.cart.controller.CartController;
import br.com.fiap.postech.adjt.cart.model.dto.request.CartItemAddRequest;
import br.com.fiap.postech.adjt.cart.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/cart")
public class CartControllerImpl implements CartController {
    private final CartService cartService;

    public CartControllerImpl(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    @PostMapping("/items")
    public ResponseEntity add(
            @RequestParam UUID consumerId,
            @RequestBody CartItemAddRequest request
    ) {
        cartService.add(consumerId, request);

        return ResponseEntity.created(null).build();
    }

    @Override
    @DeleteMapping("/item")
    public ResponseEntity remove(
            @RequestParam UUID consumerId,
            @RequestParam Long itemId
    ) {
        return null;
    }

    @Override
    @PutMapping("/item")
    public ResponseEntity increment(
            UUID consumerId,
            Long itemId) {
        return null;
    }

    @Override
    @GetMapping
    public ResponseEntity findByCustomerId(UUID consumerId) {
        return null;
    }

    @Override
    @DeleteMapping
    public ResponseEntity clear(UUID consumerId) {
        return null;
    }
}
