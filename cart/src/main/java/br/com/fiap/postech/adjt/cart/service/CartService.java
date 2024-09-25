package br.com.fiap.postech.adjt.cart.service;

import br.com.fiap.postech.adjt.cart.entity.Cart;
import br.com.fiap.postech.adjt.cart.entity.Item;
import br.com.fiap.postech.adjt.cart.exception.EmptyCartException;
import br.com.fiap.postech.adjt.cart.exception.InvalidItemException;
import br.com.fiap.postech.adjt.cart.exception.InvalidItemQuantityException;
import br.com.fiap.postech.adjt.cart.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void addItem(UUID consumerId, Long itemId, Long quantity) {
        Cart cart = cartRepository.findByConsumerId(consumerId);
        if (quantity <= 0) {
            throw new InvalidItemQuantityException();
        }
        AtomicBoolean foundItem = new AtomicBoolean(false);
        cart.getItems().forEach(cartItem -> {
            if (cartItem.getItemId().equals(itemId)) {
                foundItem.set(true);
                cartItem.setQnt(cartItem.getQnt() + quantity);
            }
        });
        if (!foundItem.get()) {
            throw new InvalidItemException();
        }
        cartRepository.save(cart);
    }

    public void removeItem(UUID consumerId, Long itemId) {
        Cart cart = cartRepository.findByConsumerId(consumerId);
        if (cart.getItems().isEmpty()) {
            throw new EmptyCartException();
        }
        AtomicReference<Boolean> toRemove = new AtomicReference<>(false);
        AtomicBoolean foundItem = new AtomicBoolean(false);
        cart.getItems().forEach(cartItem -> {
            if (cartItem.getItemId().equals(itemId)) {
                foundItem.set(true);
                if (cartItem.getQnt() == 1) {
                    toRemove.set(true);
                } else {
                    cartItem.setQnt(cartItem.getQnt() - 1);
                }
            }
        });
        if (!foundItem.get()) {
            throw new InvalidItemException();
        }
        if (toRemove.get()) {
            cart.getItems().removeIf(cartItem -> cartItem.getItemId().equals(itemId));
        }
        cartRepository.save(cart);
    }

    public void incrementItem(UUID consumerId, Long itemId) {
        Cart cart = cartRepository.findByConsumerId(consumerId);
        if (cart.getItems().isEmpty()) {
            Item item = new Item();
            item.setItemId(itemId);
            item.setQnt(Integer.toUnsignedLong(1));
            item.setCart(cart);
            cart.getItems().add(item);
            cartRepository.save(cart);
            return;
        }
        AtomicBoolean foundItem = new AtomicBoolean(false);
        cart.getItems().forEach(cartItem -> {
            if (cartItem.getItemId().equals(itemId)) {
                foundItem.set(true);
                cartItem.setQnt(cartItem.getQnt() + 1);
            }
        });
        if (!foundItem.get()) {
            throw new InvalidItemException();
        }
        cartRepository.save(cart);
    }

    public Cart findCart(UUID consumerId) {
        Cart cart = cartRepository.findByConsumerId(consumerId);
        if (cart.getItems().isEmpty()) {
            throw new EmptyCartException();
        }
        return cart;
    }

    public void removeAllItems(UUID consumerId) {
        Cart cart = cartRepository.findByConsumerId(consumerId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

}
