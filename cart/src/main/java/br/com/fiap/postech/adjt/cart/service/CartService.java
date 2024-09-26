package br.com.fiap.postech.adjt.cart.service;

import br.com.fiap.postech.adjt.cart.model.dto.request.AddCartItemRequest;
import br.com.fiap.postech.adjt.cart.model.dto.request.IncrementCartItemRequest;
import br.com.fiap.postech.adjt.cart.model.dto.request.RemoveCartItemRequest;
import br.com.fiap.postech.adjt.cart.model.entity.Cart;

import java.util.UUID;

public interface CartService {
    Cart add(AddCartItemRequest request);

    Cart remove(RemoveCartItemRequest request);

    Cart increment(IncrementCartItemRequest request);

    Cart findByCustomerId(UUID consumerId);

    Cart clear(UUID consumerId);
}
