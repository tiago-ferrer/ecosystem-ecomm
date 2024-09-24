package br.com.fiap.postech.adjt.cart.controller;

import br.com.fiap.postech.adjt.cart.model.dto.request.AddCartItemRequest;
import br.com.fiap.postech.adjt.cart.model.dto.request.ClearCartRequest;
import br.com.fiap.postech.adjt.cart.model.dto.request.FindCartByCustomerIdRequest;
import br.com.fiap.postech.adjt.cart.model.dto.request.IncrementCartItemRequest;
import br.com.fiap.postech.adjt.cart.model.dto.request.RemoveCartItemRequest;
import br.com.fiap.postech.adjt.cart.model.entity.Cart;
import org.springframework.http.ResponseEntity;

public interface CartController {
    ResponseEntity<Cart> add(
            AddCartItemRequest request
    );

    ResponseEntity<Cart> remove(
            RemoveCartItemRequest request
    );

    ResponseEntity<Cart> increment(
            IncrementCartItemRequest request
    );

    ResponseEntity<Cart> findByCustomerId(
            FindCartByCustomerIdRequest request
    );

    ResponseEntity<Cart> clear(
            ClearCartRequest request
    );
}
