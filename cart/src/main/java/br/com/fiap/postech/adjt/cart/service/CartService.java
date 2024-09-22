package br.com.fiap.postech.adjt.cart.service;

import br.com.fiap.postech.adjt.cart.ecxeptions.ErrorMessages;
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

    @Autowired
    private CartRepository cartRepository;

    public String addItem(String consumerId, String itemId, int quantity) {
        Cart cart;
        try {
            cart = validateAndGetCart(consumerId); // Tenta obter o carrinho existente
        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST && e.getReason().equals(ErrorMessages.EMPTY_CART)) {
                cart = new Cart(); // Cria um carrinho vazio
                cart.setConsumerId(UUID.fromString(consumerId)); // Define o consumerId
                cart.setItems(new ArrayList<>()); // Inicializa a lista de itens
            } else {
                throw e; // Re-lança outras exceções
            }
        }

        validateItemId(itemId); // Valida o ID do item
        validateQuantity(quantity); // Valida a quantidade

        updateCartWithItem(cart, itemId, quantity); // Atualiza o carrinho com o novo item
        cartRepository.save(cart); // Salva o carrinho atualizado
        return ErrorMessages.ITEM_ADDED_SUCCESSFULLY; // Retorna mensagem de sucesso
    }

    // Removes an item from the cart
    public String removeItem(String consumerId, String itemId) {
        Cart cart = validateAndGetCart(consumerId); // Get the cart for the consumer
        Item existingItem = getItemFromCart(cart, itemId); // Get the item to be removed
        adjustItemQuantity(cart, existingItem, -1); // Adjust quantity of the item
        cartRepository.save(cart); // Save the updated cart
        return ErrorMessages.ITEM_REMOVED_SUCCESSFULLY; // Return success message
    }

    // Increments the quantity of an item in the cart
    public String incrementItem(String consumerId, String itemId) {
        Cart cart = validateAndGetCart(consumerId); // Get the cart for the consumer
        Item existingItem = getItemFromCart(cart, itemId); // Get the item to increment
        adjustItemQuantity(cart, existingItem, 1); // Adjust quantity of the item
        cartRepository.save(cart); // Save the updated cart
        return ErrorMessages.ITEM_INCREMENTED_SUCCESSFULLY; // Return success message
    }

    // Clears all items from the cart
    public String clearCart(String consumerId) {
        Cart cart = validateAndGetCart(consumerId); // Get the cart for the consumer
        cart.getItems().clear(); // Clear all items in the cart
        cartRepository.save(cart); // Save the updated cart
        return ErrorMessages.CART_CLEARED_SUCCESSFULLY; // Return success message
    }

    // Validates consumer ID and retrieves the cart
    private Cart validateAndGetCart(String consumerId) {
        UUID consumerUUID = validateAndGetUUID(consumerId); // Validate and convert consumerId to UUID
        return getCart(consumerUUID); // Get the cart based on the consumer's UUID
    }

    // Retrieves the cart from the repository, throws an exception if empty
    public Cart getCart(UUID consumerUUID) {
        return cartRepository.findByConsumerId(consumerUUID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.EMPTY_CART));
    }

    // Validates consumer ID format and converts it to UUID
    private UUID validateAndGetUUID(String consumerId) {
        validateConsumerIdFormat(consumerId); // Validate the format of the consumer ID
        return UUID.fromString(consumerId); // Convert the valid consumer ID to UUID
    }

    // Checks if the consumer ID format is valid
    private void validateConsumerIdFormat(String consumerId) {
        if (consumerId == null || !isValidUUID(consumerId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_CONSUMER_ID_FORMAT);
        }
    }

    // Validates item ID
    private void validateItemId(String itemId) {
        if (!isValidItemId(itemId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_ITEM_ID);
        }
    }

    // Validates item quantity
    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_ITEM_QUANTITY);
        }
    }

    // Updates the cart with the specified item, adding or updating as necessary
    private void updateCartWithItem(Cart cart, String itemId, int quantity) {
        Optional<Item> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(itemId))
                .findFirst();

        existingItem.ifPresentOrElse(item -> adjustItemQuantity(cart, item, quantity),
                () -> cart.getItems().add(new Item(itemId, quantity))); // Add new item if it does not exist
    }

    // Adjusts the quantity of an item in the cart, removing it if quantity becomes zero or less
    private void adjustItemQuantity(Cart cart, Item item, int adjustment) {
        int newQuantity = item.getQuantity() + adjustment; // Calculate new quantity
        if (newQuantity <= 0) {
            cart.getItems().remove(item); // Remove item if quantity is zero or less
        } else {
            item.setQuantity(newQuantity); // Update item quantity
        }
    }

    // Retrieves an item from the cart, throwing an exception if it does not exist
    private Item getItemFromCart(Cart cart, String itemId) {
        return cart.getItems().stream()
                .filter(item -> item.getProductId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessages.INVALID_ITEM_ID));
    }

    // Validates UUID format
    private boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid); // Attempt to create UUID from string
            return true; // Return true if successful
        } catch (IllegalArgumentException e) {
            return false; // Return false if an exception is thrown
        }
    }

    // Checks if item ID is valid
    private boolean isValidItemId(String itemId) {
        return itemId != null && !itemId.trim().isEmpty(); // Return true if item ID is non-null and not empty
    }
}
