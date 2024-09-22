package br.com.fiap.postech.adjt.cart.service;

import br.com.fiap.postech.adjt.cart.model.dto.request.CartItemAddRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface CartService {
    void add(UUID consumerId, CartItemAddRequest request);

    void remove(UUID consumerId, Long itemId);

    void increment(UUID consumerId, Long itemId);

    void findByCustomerId(UUID consumerId);

    void clear(UUID consumerId);
}
