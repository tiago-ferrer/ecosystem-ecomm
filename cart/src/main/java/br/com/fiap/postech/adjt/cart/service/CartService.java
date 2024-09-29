package br.com.fiap.postech.adjt.cart.service;

import br.com.fiap.postech.adjt.cart.dto.AddItemToCartDTO;
import br.com.fiap.postech.adjt.cart.model.Cart;
import br.com.fiap.postech.adjt.cart.repository.CartRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public ResponseEntity<?> addItemToCart(UUID consumerId, AddItemToCartDTO addItemToCartDTO) {
        if (consumerId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid consumerId format"));
        }

        if (addItemToCartDTO.itemId() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid itemId does not exist"));
        }

        if (addItemToCartDTO.quantity() <= 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid itemId quantity"));
        }

        List<Cart> existingCartItems = cartRepository.findByConsumerId(consumerId);
        Optional<Cart> existingCartItem = existingCartItems.stream()
                .filter(cart -> cart.getItemId().equals(addItemToCartDTO.itemId()))
                .findFirst();

        if (existingCartItem.isPresent()) {
            Cart cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + addItemToCartDTO.quantity());
            cartRepository.save(cartItem);
        } else {
            Cart newCartItem = new Cart(consumerId, addItemToCartDTO.itemId(), addItemToCartDTO.quantity());
            cartRepository.save(newCartItem);
        }

        List<Cart> updatedCartItems = cartRepository.findByConsumerId(consumerId);
        return ResponseEntity.ok(Map.of("message", "Item added to cart successfully", "cartItems", updatedCartItems));
    }



    public List<Cart> getCartItems(UUID consumerId) {
        return cartRepository.findByConsumerId(consumerId);
    }
    public ResponseEntity<?> removeItemFromCart(String consumerIdStr, Long itemId) {
        if (!isValidUUID(consumerIdStr)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid consumerId format"));
        }

        UUID consumerId = UUID.fromString(consumerIdStr);

        Optional<Cart> cartItem = cartRepository.findByConsumerIdAndItemId(consumerId, itemId);

        if (cartItem.isPresent()) {
            cartRepository.delete(cartItem.get());
            return ResponseEntity.ok(Map.of("message", "Item removed from cart successfully"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Item not found in cart"));
        }
    }


    public ResponseEntity<?> removeAllItemsFromCart(String consumerIdStr) {
        if (!isValidUUID(consumerIdStr)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid consumerId format"));
        }

        UUID consumerId = UUID.fromString(consumerIdStr);
        List<Cart> cartItems = cartRepository.findByConsumerId(consumerId);

        if (cartItems.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "No items found in the cart"));
        }

        cartRepository.deleteAll(cartItems);
        return ResponseEntity.ok(Map.of("message", "All items removed from cart successfully"));
    }

    private boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
