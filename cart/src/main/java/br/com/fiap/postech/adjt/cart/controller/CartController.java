package br.com.fiap.postech.adjt.cart.controller;

import br.com.fiap.postech.adjt.cart.model.dto.request.*;
import br.com.fiap.postech.adjt.cart.model.entity.Cart;
import org.springframework.http.ResponseEntity;

public interface CartController {
    ResponseEntity<Cart> add(AddCartItemRequest request);

    ResponseEntity<Cart> remove(RemoveCartItemRequest request);

    ResponseEntity<Cart> increment(IncrementCartItemRequest request);

    ResponseEntity<Cart> findByCustomerId(FindCartByCustomerIdRequest request);

    ResponseEntity<Cart> clear(ClearCartRequest request);
}
