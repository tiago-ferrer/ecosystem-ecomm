package br.com.fiap.postech.adjt.cart.controller;

import br.com.fiap.postech.adjt.cart.dto.CartDto;
import br.com.fiap.postech.adjt.cart.dto.CartRequest;
import br.com.fiap.postech.adjt.cart.dto.CartResponseRecord;
import br.com.fiap.postech.adjt.cart.service.CartService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<CartResponseRecord> addItem(
            @JsonView({CartRequest.CartView.AddItem.class})
            @Validated({CartRequest.CartView.AddItem.class})
            @RequestBody CartRequest cartRequest) {
        return cartService.addItem(cartRequest);
    }

    @DeleteMapping("/item")
    public ResponseEntity<CartResponseRecord> removeItem(
            @JsonView({CartRequest.CartView.Update.class})
            @Validated({CartRequest.CartView.Update.class})
            @RequestBody CartRequest cartRequest) {
        return cartService.decrementItem(cartRequest);
    }

    @PutMapping("/item")
    public ResponseEntity<CartResponseRecord> incrementItem(
            @JsonView({CartRequest.CartView.Update.class})
            @Validated({CartRequest.CartView.Update.class})
            @RequestBody CartRequest cartRequest) {
        return cartService.incrementItem(cartRequest);
    }


    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CartDto> findCartByConsumerId(
            @JsonView({CartRequest.CartView.GetDelete.class})
            @Validated({CartRequest.CartView.GetDelete.class})
            @RequestBody CartRequest cartRequest) {
        return cartService.findCartByConsumerId(cartRequest);
    }


    @DeleteMapping
    public ResponseEntity<CartResponseRecord> deleteCartByConsumerId(
            @JsonView({CartRequest.CartView.GetDelete.class})
            @Validated({CartRequest.CartView.GetDelete.class})
            @RequestBody CartRequest cartRequest) {
        return cartService.deleteCartByConsumerId(cartRequest);
    }

}
