package br.com.fiap.postech.adjt.cart.service;

import br.com.fiap.postech.adjt.cart.dto.CartDto;
import br.com.fiap.postech.adjt.cart.dto.CartRequest;
import br.com.fiap.postech.adjt.cart.dto.CartResponseRecord;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface CartService {

    ResponseEntity<CartResponseRecord> addItem(CartRequest cartRequest);
    ResponseEntity<CartResponseRecord> decrementItem(CartRequest cartRequest);
    ResponseEntity<CartResponseRecord> incrementItem(CartRequest cartRequest);
    ResponseEntity<CartDto> findCartByConsumerId(CartRequest cartRequest);
    ResponseEntity<CartResponseRecord> deleteCartByConsumerId(CartRequest cartRequest);


}
