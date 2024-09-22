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
    private static final String ITEM_ADDED_SUCCESSFULLY = "Item added to cart successfully";
    private static final String ITEM_REMOVED_SUCCESSFULLY = "Item removed from cart successfully";
    private static final String ITEM_INCREMENTED_SUCCESSFULLY = "Item incremented successfully";
    private static final String CART_CLEARED_SUCCESSFULLY = "Items removed from cart successfully";

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
        return ITEM_ADDED_SUCCESSFULLY;
    }

    public String removeItem(String consumerId, String itemId) {
        validateConsumerIdFormat(consumerId);
        Cart cart = getCart(consumerId);

        Item existingItem = findItemInCart(cart, itemId);
        adjustItemQuantity(cart, existingItem, -1);
        return ITEM_REMOVED_SUCCESSFULLY;
    }

    public String incrementItem(String consumerId, String itemId) {
        validateConsumerIdFormat(consumerId);
        Cart cart = getCart(consumerId);
        Item existingItem = findItemInCart(cart, itemId);
        adjustItemQuantity(cart, existingItem, 1);
        return ITEM_INCREMENTED_SUCCESSFULLY;
    }

    public Cart getCart(String consumerId) {
        validateConsumerIdFormat(consumerId);
        UUID consumerUUID = UUID.fromString(consumerId);
        return cartRepository.findByConsumerId(consumerUUID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, EMPTY_CART));
    }

    public String clearCart(String consumerId) {
        validateConsumerIdFormat(consumerId);
        UUID consumerUUID = UUID.fromString(consumerId);
        Cart cart = getCart(consumerUUID.toString());
        cart.getItems().clear();
        cartRepository.save(cart);
        return CART_CLEARED_SUCCESSFULLY;
    }

    public void validateConsumerIdFormat(String consumerId) {
        if (consumerId == null || !isValidUUID(consumerId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_CONSUMER_ID_FORMAT);
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
            adjustItemQuantity(cart, existingItem.get(), quantity);
        } else {
            cart.getItems().add(new Item(itemId, quantity));
        }
    }

    private void adjustItemQuantity(Cart cart, Item item, int adjustment) {
        int newQuantity = item.getQuantity() + adjustment;
        if (newQuantity <= 0) {
            cart.getItems().remove(item);
        } else {
            item.setQuantity(newQuantity);
        }
        cartRepository.save(cart);
    }

    private Item findItemInCart(Cart cart, String itemId) {
        return cart.getItems().stream()
                .filter(item -> item.getProductId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_ITEM_ID));
    }

    private boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isValidItemId(String itemId) {
        return itemId != null && !itemId.trim().isEmpty();
    }
}

