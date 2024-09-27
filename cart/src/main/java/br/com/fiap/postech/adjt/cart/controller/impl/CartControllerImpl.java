package br.com.fiap.postech.adjt.cart.controller.impl;

import br.com.fiap.postech.adjt.cart.controller.CartController;
import br.com.fiap.postech.adjt.cart.model.dto.request.*;
import br.com.fiap.postech.adjt.cart.model.dto.response.MessageResponse;
import br.com.fiap.postech.adjt.cart.model.entity.Cart;
import br.com.fiap.postech.adjt.cart.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartControllerImpl implements CartController {
    private final CartService cartService;

    public CartControllerImpl(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    @PostMapping("/items")
    public ResponseEntity<MessageResponse> add(@RequestBody AddCartItemRequest request) {
        cartService.add(request);

        return ResponseEntity.ok(new MessageResponse("Item added to cart successfully"));
    }

    @Override
    @DeleteMapping("/item")
    public ResponseEntity<MessageResponse> remove(@RequestBody RemoveCartItemRequest request) {
        cartService.remove(request);

        return ResponseEntity.ok(new MessageResponse("Item removed from cart successfully"));
    }

    @Override
    @PutMapping("/item")
    public ResponseEntity<MessageResponse> increment(@RequestBody IncrementCartItemRequest request) {
        cartService.increment(request);

        return ResponseEntity.ok(new MessageResponse("Item removed from cart successfully"));
    }

    @Override
    @GetMapping
    public ResponseEntity<Cart> findByCustomerId(@RequestBody FindCartByCustomerIdRequest request) {
        Cart cart = cartService.findByCustomerId(request.consumerId());

        return ResponseEntity.ok(cart);
    }

    @Override
    @DeleteMapping
    public ResponseEntity<Cart> clear(@RequestBody ClearCartRequest request) {
        Cart cart = cartService.clear(request.consumerId());

        return ResponseEntity.ok(cart);
    }
}
