package br.com.fiap.postech.adjt.checkout.service;


import br.com.fiap.postech.adjt.checkout.dto.cart.CartDto;
import br.com.fiap.postech.adjt.checkout.dto.cart.CartRequest;
import br.com.fiap.postech.adjt.checkout.dto.cart.CartResponseRecord;
import org.springframework.http.ResponseEntity;

public interface CartService {
    CartDto findByConsumerId (CartRequest cartRequest);

    ResponseEntity<CartResponseRecord> deleteByConsumerId(CartRequest cartRequest);

}
