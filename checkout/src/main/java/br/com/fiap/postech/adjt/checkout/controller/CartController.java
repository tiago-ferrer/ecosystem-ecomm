package br.com.fiap.postech.adjt.checkout.controller;

import br.com.fiap.postech.adjt.checkout.config.HttpClientCustom;
import br.com.fiap.postech.adjt.checkout.dto.cart.CartDto;
import br.com.fiap.postech.adjt.checkout.dto.cart.CartRequest;
import br.com.fiap.postech.adjt.checkout.service.CartService;
import br.com.fiap.postech.adjt.checkout.service.impl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {


    @Autowired
    private CartService service;


    @GetMapping
    public ResponseEntity<CartDto> findByConsumerId(@RequestBody CartRequest cartRequest) {
        var  cartDto = service.findByConsumerId(cartRequest);
        if (cartDto == null){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cartDto);
    }


}
