package br.com.fiap.postech.adjt.checkout.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.fiap.postech.adjt.checkout.config.HttpClientCustom;
import br.com.fiap.postech.adjt.checkout.consumer.CartClient;
import br.com.fiap.postech.adjt.checkout.dto.cart.CartDto;
import br.com.fiap.postech.adjt.checkout.dto.cart.CartRequest;
import br.com.fiap.postech.adjt.checkout.dto.cart.CartResponseRecord;
import br.com.fiap.postech.adjt.checkout.entity.cart.Cart;
import br.com.fiap.postech.adjt.checkout.entity.cart.Item;
import br.com.fiap.postech.adjt.checkout.exception.CartConsumerException;
import br.com.fiap.postech.adjt.checkout.exception.CartNotFoundException;
import br.com.fiap.postech.adjt.checkout.repository.CartRepository;
import br.com.fiap.postech.adjt.checkout.repository.ItemRepository;
import br.com.fiap.postech.adjt.checkout.service.CartService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartClient cartClient;
    private final HttpClientCustom httpClientCustom;

    @Value("${URL_CART}")
    private String urlCart;


    public CartServiceImpl(CartClient cartClient,
                           HttpClientCustom httpClientCustom,
                           CartRepository cartRepository,
                           ItemRepository itemRepository) {
        this.cartClient = cartClient;
        this.httpClientCustom = httpClientCustom;
    }


    @Override
    public CartDto findByConsumerId(CartRequest cartRequest) {
        try {
            ResponseEntity<Object> responseEntity = httpClientCustom.handler(urlCart, HttpMethod.GET, cartRequest);
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseBody = objectMapper.convertValue(responseEntity.getBody(), new TypeReference<Map<String, Object>>() {});
            CartDto cartDto = new CartDto();
            cartDto =  cartDto.convertResponseEntityToCartDto(responseBody);
            if(cartDto == null || cartDto.getItems().isEmpty())
                 throw new CartNotFoundException("Cart not found");
            else {
                return cartDto;
            }
        } catch (CartConsumerException e){
             throw new CartConsumerException("Invalid Consumer");
        }
    }


    public ResponseEntity<CartResponseRecord> deleteByConsumerId(CartRequest cartRequest) {
        return cartClient.deleteByConsumerId(cartRequest);
    }
}
