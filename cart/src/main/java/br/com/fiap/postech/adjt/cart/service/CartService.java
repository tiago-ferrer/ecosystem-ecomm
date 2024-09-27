package br.com.fiap.postech.adjt.cart.service;

import br.com.fiap.postech.adjt.cart.model.dto.request.AddCartItemRequest;
import br.com.fiap.postech.adjt.cart.model.dto.request.IncrementCartItemRequest;
import br.com.fiap.postech.adjt.cart.model.dto.request.RemoveCartItemRequest;
import br.com.fiap.postech.adjt.cart.model.dto.response.FindCartByCustomerIdResponse;

public interface CartService {
    void add(AddCartItemRequest request);

    void remove(RemoveCartItemRequest request);

    void increment(IncrementCartItemRequest request);

    FindCartByCustomerIdResponse findByCustomerId(String consumerId);

    void clear(String consumerId);
}
