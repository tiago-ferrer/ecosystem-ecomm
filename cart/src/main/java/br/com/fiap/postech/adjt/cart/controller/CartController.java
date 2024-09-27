package br.com.fiap.postech.adjt.cart.controller;

import br.com.fiap.postech.adjt.cart.model.dto.request.*;
import br.com.fiap.postech.adjt.cart.model.dto.response.MessageResponse;
import br.com.fiap.postech.adjt.cart.model.entity.Cart;
import org.springframework.http.ResponseEntity;

public interface CartController {
    ResponseEntity<MessageResponse> add(AddCartItemRequest request);

    ResponseEntity<MessageResponse> remove(RemoveCartItemRequest request);

    ResponseEntity<MessageResponse> increment(IncrementCartItemRequest request);

    ResponseEntity<Cart> findByCustomerId(FindCartByCustomerIdRequest request);

    ResponseEntity<Cart> clear(ClearCartRequest request);
}
