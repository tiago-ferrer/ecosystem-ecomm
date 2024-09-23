package br.com.fiap.postech.adjt.cart.controller;

import br.com.fiap.postech.adjt.cart.dto.AddOrRemoveItemRequest;
import br.com.fiap.postech.adjt.cart.dto.CartResponse;
import br.com.fiap.postech.adjt.cart.dto.ConsumerIdRequest;
import br.com.fiap.postech.adjt.cart.dto.ItemRequest;
import br.com.fiap.postech.adjt.cart.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    private static final String ITEM_CREATED_SUCCESS_MESSAGE = "Item added to cart successfully";
    private static final String ITEM_REMOVE_SUCCESS_MESSAGE = "Item removed from cart successfully";
    private static final String ALL_ITEM_REMOVE_SUCCESS_MESSAGE = "Items removed from cart successfully";
    private static final String ITEM_ADD_SUCCESS_MESSAGE = "Item added from cart successfully";

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/items")
    public ResponseEntity<String> createCartItem(@RequestBody ItemRequest request) {
        cartService.createCartItem(request);
        return ResponseEntity.ok(ITEM_CREATED_SUCCESS_MESSAGE);
    }

    @DeleteMapping("/item")
    public ResponseEntity<String> removeItemFromCart(@RequestBody AddOrRemoveItemRequest request) {
        cartService.removeItemFromCart(request);
        return ResponseEntity.ok(ITEM_REMOVE_SUCCESS_MESSAGE);
    }

    @PutMapping("/item")
    public ResponseEntity<String> addItemFromCart(@RequestBody AddOrRemoveItemRequest request) {
        cartService.addItemToCart(request);
        return ResponseEntity.ok(ITEM_ADD_SUCCESS_MESSAGE);
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(@RequestBody ConsumerIdRequest request) {
        CartResponse cartResponse = cartService.getCart(request.consumerId());
        return ResponseEntity.ok(cartResponse);

    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllItens(@RequestBody ConsumerIdRequest request) {
        cartService.deleteAllItens(request.consumerId());
        return ResponseEntity.ok(ALL_ITEM_REMOVE_SUCCESS_MESSAGE);
    }
}
