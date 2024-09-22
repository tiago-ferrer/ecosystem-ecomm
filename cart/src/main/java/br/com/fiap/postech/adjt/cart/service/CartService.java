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
    @Autowired
    private CartRepository cartRepository;

    public String addItem(String consumerId, String itemId, int quantity) {
        if (!isValidUUID(consumerId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid consumerId format");
        }
        if (quantity <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid itemId quantity");
        }

        Cart cart = cartRepository.findByConsumerId(consumerId);
        if (cart == null) {
            cart = new Cart(consumerId, new ArrayList<>());
        }

        Optional<Item> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(itemId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            cart.getItems().add(new Item(null, itemId, quantity));
        }

        cartRepository.save(cart);
        return "Item added to cart successfully";
    }

    public String removeItem(String consumerId, String itemId) {
        if (!isValidUUID(consumerId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid consumerId format");
        }

        Cart cart = cartRepository.findByConsumerId(consumerId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empty cart");
        }

        Optional<Item> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(itemId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() - 1);
            if (existingItem.get().getQuantity() <= 0) {
                cart.getItems().remove(existingItem.get());
            }
            cartRepository.save(cart);
            return "Item removed from cart successfully";
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid itemId");
        }
    }

    public String incrementItem(String consumerId, String itemId) {
        if (!isValidUUID(consumerId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid consumerId format");
        }

        Cart cart = cartRepository.findByConsumerId(consumerId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empty cart");
        }

        Optional<Item> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(itemId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + 1);
            cartRepository.save(cart);
            return "Item incremented successfully";
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid itemId");
        }
    }

    public Cart getCart(String consumerId) {
        if (!isValidUUID(consumerId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid consumerId format");
        }

        Cart cart = cartRepository.findByConsumerId(consumerId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empty cart");
        }

        return cart;
    }

    public String clearCart(String consumerId) {
        if (!isValidUUID(consumerId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid consumerId format");
        }

        Cart cart = cartRepository.findByConsumerId(consumerId);
        if (cart != null) {
            cart.getItems().clear();
            cartRepository.save(cart);
        }
        return "Items removed from cart successfully";
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
