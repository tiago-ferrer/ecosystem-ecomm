package service;

import domain.Cart;
import domain.Item;
import exception.CartNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.CartRepository;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public Cart addItem(UUID consumerId, Item item) {
        // Verifica se o item é nulo
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        // Busca o carrinho pelo consumerId ou cria um novo se não existir
        Cart cart = cartRepository.findByConsumerId(consumerId)
                .orElseGet(() -> new Cart(consumerId)); // Apenas o consumerId

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
