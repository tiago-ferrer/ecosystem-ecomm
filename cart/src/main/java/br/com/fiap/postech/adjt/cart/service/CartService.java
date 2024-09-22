package br.com.fiap.postech.adjt.cart.service;

import br.com.fiap.postech.adjt.cart.model.Cart;
import br.com.fiap.postech.adjt.cart.model.Item;
import br.com.fiap.postech.adjt.cart.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {

    private static final String INVALID_CONSUMER_ID_FORMAT = "Invalid consumerId format";
    private static final String EMPTY_CART = "Empty cart";
    private static final String INVALID_ITEM_ID = "Invalid itemId does not exist";
    private static final String INVALID_ITEM_QUANTITY = "Invalid itemId quantity";

    @Autowired
    private CartRepository cartRepository;

    public String addItem(String consumerId, String itemId, int quantity) {
        validateConsumerIdFormat(consumerId);
        UUID consumerUUID = UUID.fromString(consumerId);
        validateItemId(itemId);
        validateQuantity(quantity);

        Cart cart = getOrCreateCart(consumerUUID);
        updateCartWithItem(cart, itemId, quantity);
        cartRepository.save(cart);
        return "Item added to cart successfully";
    }

    public String removeItem(String consumerId, String itemId) {
        validateConsumerIdFormat(consumerId);
        UUID consumerUUID = UUID.fromString(consumerId); // Converte para UUID
        Cart cart = getCart(consumerUUID);

        Item existingItem = findItemInCart(cart, itemId);
        existingItem.setQuantity(existingItem.getQuantity() - 1);
        if (existingItem.getQuantity() <= 0) {
            cart.getItems().remove(existingItem);
        }
        cartRepository.save(cart);
        return "Item removed from cart successfully";
    }

    public String incrementItem(String consumerId, String itemId) {
        validateConsumerIdFormat(consumerId);
        UUID consumerUUID = UUID.fromString(consumerId);

        Cart cart = getCart(consumerUUID); // Agora usando UUID
        Item existingItem = findItemInCart(cart, itemId);
        existingItem.setQuantity(existingItem.getQuantity() + 1);
        cartRepository.save(cart);
        return "Item incremented successfully";
    }
    public Cart getCart(UUID consumerId) {
        validateConsumerId(consumerId);
        return cartRepository.findByConsumerId(consumerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, EMPTY_CART));
    }

    public String clearCart(UUID consumerId) {
        validateConsumerId(consumerId);
        Cart cart = getCart(consumerId);
        cart.getItems().clear();
        cartRepository.save(cart);
        return "Items removed from cart successfully";
    }

    private void validateConsumerId(UUID consumerId) {
        if (consumerId == null || !isValidUUID(consumerId.toString())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_CONSUMER_ID_FORMAT);
        }
    }

    private void validateConsumerIdFormat(String consumerId) {
        if (consumerId == null || !isValidUUID(consumerId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_CONSUMER_ID_FORMAT);
        }
    }

    private boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private void validateItemId(String itemId) {
        if (!isValidItemId(itemId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_ITEM_ID);
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_ITEM_QUANTITY);
        }
    }

    private Cart getOrCreateCart(UUID consumerId) {
        return cartRepository.findByConsumerId(consumerId)
                .orElseGet(() -> new Cart(consumerId, new ArrayList<>()));
    }

    private void updateCartWithItem(Cart cart, String itemId, int quantity) {
        Optional<Item> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(itemId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            Item newItem = new Item(itemId, quantity);
            cart.getItems().add(newItem);
        }
    }

    private Item findItemInCart(Cart cart, String itemId) {
        return cart.getItems().stream()
                .filter(item -> item.getProductId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_ITEM_ID));
    }

    private boolean isValidItemId(String itemId) {
        return itemId != null && !itemId.trim().isEmpty();
    }
}
