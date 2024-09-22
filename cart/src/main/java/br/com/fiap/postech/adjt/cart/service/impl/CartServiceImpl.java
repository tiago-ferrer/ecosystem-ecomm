package br.com.fiap.postech.adjt.cart.service.impl;

import br.com.fiap.postech.adjt.cart.model.dto.request.CartItemAddRequest;
import br.com.fiap.postech.adjt.cart.service.CartService;

import java.util.UUID;

public class CartServiceImpl implements CartService {
    @Override
    public void add(UUID consumerId, CartItemAddRequest request) {
        if (request.itemId() == null || item) {
            throw new IllegalArgumentException("Invalid itemId does not exist");
        } else if (request.quantity() == null || request.quantity().isEmpty()
                || request.quantity().isBlank() || Integer.parseInt(request.quantity()) <= 0){
            throw new IllegalArgumentException("Invalid itemId quantity");
        }

    }

    @Override
    public void remove(UUID consumerId, Long itemId) {

    }

    @Override
    public void increment(UUID consumerId, Long itemId) {

    }

    @Override
    public void findByCustomerId(UUID consumerId) {

    }

    @Override
    public void clear(UUID consumerId) {

    }
}
