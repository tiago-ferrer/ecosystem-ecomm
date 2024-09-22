package br.com.fiap.postech.adjt.cart.controller;

import br.com.fiap.postech.adjt.cart.model.dto.request.CartItemAddRequest;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface CartController {
    ResponseEntity add(
            UUID consumerId,
            CartItemAddRequest request
    );

    ResponseEntity remove(
            UUID consumerId,
            Long itemId
    );

    ResponseEntity increment(
            UUID consumerId,
            Long itemId
    );

    ResponseEntity findByCustomerId(
            UUID consumerId
    );

    ResponseEntity clear(
            UUID consumerId
    );
}
