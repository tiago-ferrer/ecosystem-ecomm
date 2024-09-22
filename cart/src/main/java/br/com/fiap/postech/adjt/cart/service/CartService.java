package br.com.fiap.postech.adjt.cart.service;

import br.com.fiap.postech.adjt.cart.ecxeptions.ErrorMessages;
import br.com.fiap.postech.adjt.cart.model.Cart;
import br.com.fiap.postech.adjt.cart.model.Item;
import br.com.fiap.postech.adjt.cart.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public String addItem(String consumerId, String itemId, int quantity) {
        Cart cart = validateAndGetCart(consumerId);
        validateItemId(itemId);
        validateQuantity(quantity);

        updateCartWithItem(cart, itemId, quantity);
        cartRepository.save(cart);
        return ErrorMessages.ITEM_ADDED_SUCCESSFULLY;
    }

    public String removeItem(String consumerId, String itemId) {
        Cart cart = validateAndGetCart(consumerId);
        Item existingItem = getItemFromCart(cart, itemId);
        adjustItemQuantity(cart, existingItem, -1);
        cartRepository.save(cart); // Salvar o carrinho após a remoção
        return ErrorMessages.ITEM_REMOVED_SUCCESSFULLY;
    }

    public String incrementItem(String consumerId, String itemId) {
        Cart cart = validateAndGetCart(consumerId);
        Item existingItem = getItemFromCart(cart, itemId);
        adjustItemQuantity(cart, existingItem, 1);
        cartRepository.save(cart); // Salvar o carrinho após a incrementação
        return ErrorMessages.ITEM_INCREMENTED_SUCCESSFULLY;
    }

    public String clearCart(String consumerId) {
        Cart cart = validateAndGetCart(consumerId);
        cart.getItems().clear();
        cartRepository.save(cart);
        return ErrorMessages.CART_CLEARED_SUCCESSFULLY;
    }

    private Cart validateAndGetCart(String consumerId) {
        UUID consumerUUID = validateAndGetUUID(consumerId);
        return getCart(consumerUUID);
    }

    public Cart getCart(UUID consumerUUID) {
        return cartRepository.findByConsumerId(consumerUUID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.EMPTY_CART));
    }

    private UUID validateAndGetUUID(String consumerId) {
        validateConsumerIdFormat(consumerId);
        return UUID.fromString(consumerId);
    }

    private void validateConsumerIdFormat(String consumerId) {
        if (consumerId == null || !isValidUUID(consumerId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_CONSUMER_ID_FORMAT);
        }
    }

    private void validateItemId(String itemId) {
        if (!isValidItemId(itemId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_ITEM_ID);
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_ITEM_QUANTITY);
        }
    }

    private void updateCartWithItem(Cart cart, String itemId, int quantity) {
        Optional<Item> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(itemId))
                .findFirst();

        existingItem.ifPresentOrElse(item -> adjustItemQuantity(cart, item, quantity),
                () -> cart.getItems().add(new Item(itemId, quantity)));
    }

    private void adjustItemQuantity(Cart cart, Item item, int adjustment) {
        int newQuantity = item.getQuantity() + adjustment;
        if (newQuantity <= 0) {
            cart.getItems().remove(item);
        } else {
            item.setQuantity(newQuantity);
        }
    }

    private Item getItemFromCart(Cart cart, String itemId) {
        return cart.getItems().stream()
                .filter(item -> item.getProductId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_ITEM_ID));
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
