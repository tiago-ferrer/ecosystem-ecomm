package br.com.fiap.postech.adjt.cart.service;

import br.com.fiap.postech.adjt.cart.dto.*;
import br.com.fiap.postech.adjt.cart.exception.EmptyCartException;
import br.com.fiap.postech.adjt.cart.exception.InvalidConsumerIdException;
import br.com.fiap.postech.adjt.cart.exception.InvalidItemIdException;
import br.com.fiap.postech.adjt.cart.exception.InvalidItemQuantityException;
import br.com.fiap.postech.adjt.cart.feign.ProductClient;
import br.com.fiap.postech.adjt.cart.model.Cart;
import br.com.fiap.postech.adjt.cart.model.Item;
import br.com.fiap.postech.adjt.cart.repository.CartItemRepository;
import br.com.fiap.postech.adjt.cart.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductClient productClient;

    public CartServiceImpl(CartItemRepository cartItemRepository,
                           ProductClient productClient,
                           CartRepository cartRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productClient = productClient;
        this.cartRepository = cartRepository;
    }

    @Override
    @Transactional
    public void createCartItem(ItemRequest request) {
        UUID consumerId = getUUID(request.consumerId());
        ProductDto product = getProduct(request.itemId());
        validateQuantity(request.quantity());
        Cart cart = createCart(consumerId);
        Item item = createItem(request, consumerId, product, cart);
        cartItemRepository.save(item);
    }

    @Override
    @Transactional
    public void removeItemFromCart(AddOrRemoveItemRequest request) {
        UUID consumerId = getUUID(request.consumerId());
        Item item = getItem(request, consumerId);
        updateItemQuantity(item, -1);
        removeItem(item);
    }

    @Override
    @Transactional
    public void addItemToCart(AddOrRemoveItemRequest request) {
        UUID consumerId = getUUID(request.consumerId());
        Item item = getItem(request, consumerId);
        updateItemQuantity(item, 1);
        cartItemRepository.save(item);
    }

    @Override
    @Transactional
    public CartResponse getCart(String consumerId) {
        UUID consumerUUID = getUUID(consumerId);
        Cart cart = cartRepository.findByConsumerId(consumerUUID)
                .orElseThrow(() -> new EmptyCartException("Empty cart"));

        List<Item> items = cartItemRepository.findByCart_CartId(cart.getCartId());

        if (items.isEmpty()) {
            throw new EmptyCartException("Empty cart");
        }

        List<ItemResponse> itemResponses = items.stream()
                .map(item -> new ItemResponse(item.getItemId(), item.getQuantity()))
                .toList();

        return new CartResponse(itemResponses);
    }

    @Override
    @Transactional
    public void deleteAllItens(String consumerId) {
        UUID consumerUUID = getUUID(consumerId);
        Cart cart = cartRepository.findByConsumerId(consumerUUID)
                .orElseThrow(() -> new EmptyCartException("Cart not found"));

        List<Item> items = cartItemRepository.findByCart_CartId(cart.getCartId());
        cartItemRepository.deleteAll(items);
    }

    private void updateItemQuantity(Item item, int quantityChange) {
        if (item.getQuantity() + quantityChange > 0) {
            item.setQuantity(item.getQuantity() + quantityChange);
        } else {
            cartItemRepository.delete(item);
        }
    }

    private void removeItem(Item item) {
        if (item.getQuantity() <= 0) {
            cartItemRepository.delete(item);
        } else {
            if (cartItemRepository.existsById(item.getItemId())) {
                cartItemRepository.save(item);
            }
        }
    }

    private Item getItem(AddOrRemoveItemRequest request, UUID consumerId) {
        return cartItemRepository.findByConsumerIdAndItemId(consumerId, request.itemId())
                .orElseThrow(() -> new InvalidItemIdException("Invalid itemId"));
    }

    private Item createItem(ItemRequest request, UUID consumerId, ProductDto product, Cart cart) {
        Item item = cartItemRepository.findByConsumerIdAndItemId(consumerId, request.itemId())
                .orElseGet(() -> {
                    Item newItem = new Item();
                    newItem.setConsumerId(consumerId);
                    newItem.setItemId(request.itemId());
                    newItem.setPrice(product.price());
                    newItem.setCart(cart);
                    return newItem;
                });
        item.setQuantity(request.quantity());
        return item;
    }

    private Cart createCart(UUID consumerId) {
        return cartRepository.findByConsumerId(consumerId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setConsumerId(consumerId);
                    return cartRepository.save(newCart);
                });
    }

    private UUID getUUID(String uuidString) {
        try {
            return UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            throw new InvalidConsumerIdException("Invalid consumerId format");
        }
    }

    private ProductDto getProduct(Long itemId) {
        if (itemId == null) {
            throw new InvalidItemIdException("Invalid itemId does not exist");
        }
        ProductDto product = productClient.getProductById(itemId);
        if (product == null) {
            throw new InvalidItemIdException("Invalid itemId does not exist");
        }
        return product;
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new InvalidItemQuantityException("Invalid item quantity");
        }
    }

}
