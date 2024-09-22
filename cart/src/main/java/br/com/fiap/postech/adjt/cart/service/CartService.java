package br.com.fiap.postech.adjt.cart.service;

import br.com.fiap.postech.adjt.cart.domain.Cart;
import br.com.fiap.postech.adjt.cart.domain.Item;
import br.com.fiap.postech.adjt.cart.exception.CartNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.fiap.postech.adjt.cart.repository.CartRepository;

import java.util.UUID;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public Cart addItem(UUID consumerId, Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        // Cria um novo carrinho se não existir
        Cart cart = cartRepository.findByConsumerId(consumerId)
                .orElseGet(() -> {
                    Cart newCart = new Cart(consumerId);
                    return cartRepository.save(newCart); // Salva o novo carrinho
                });

        // Verifica se o item já está no carrinho
        boolean itemExists = cart.getItems().stream()
                .anyMatch(existingItem -> existingItem.getId().equals(item.getId()));

        if (itemExists) {
            throw new IllegalArgumentException("Item already exists in the cart");
        }

        // Adiciona o item ao carrinho
        cart.getItems().add(item);

        // Salva o carrinho atualizado
        return cartRepository.save(cart);
    }


    public Cart removeItem(UUID consumerId, UUID itemId) {
        Cart cart = getCartByConsumerId(consumerId);
        boolean itemRemoved = cart.getItems().removeIf(item -> item.getId().equals(itemId));

        if (!itemRemoved) {
            throw new IllegalArgumentException("Item with id " + itemId + " not found in cart");
        }

        return cartRepository.save(cart);
    }

    public Cart incrementItem(UUID consumerId, UUID itemId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        Cart cart = getCartByConsumerId(consumerId);
        cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .ifPresent(item -> item.setQuantity(item.getQuantity() + quantity));

        return cartRepository.save(cart);
    }

    public void clearCart(UUID consumerId) {
        Cart cart = getCartByConsumerId(consumerId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    private Cart getCartByConsumerId(UUID consumerId) {
        return cartRepository.findByConsumerId(consumerId)
                .orElseThrow(() -> new CartNotFoundException(consumerId));
    }
}
