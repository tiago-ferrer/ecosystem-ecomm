package br.com.fiap.postech.adjt.cart.service.impl;

import br.com.fiap.postech.adjt.cart.clients.ItemsClient;
import br.com.fiap.postech.adjt.cart.controller.exception.NotFoundException;
import br.com.fiap.postech.adjt.cart.model.dto.request.AddCartItemRequest;
import br.com.fiap.postech.adjt.cart.model.dto.request.IncrementCartItemRequest;
import br.com.fiap.postech.adjt.cart.model.dto.request.RemoveCartItemRequest;
import br.com.fiap.postech.adjt.cart.model.dto.response.ItemResponse;
import br.com.fiap.postech.adjt.cart.model.entity.Cart;
import br.com.fiap.postech.adjt.cart.model.entity.CartItem;
import br.com.fiap.postech.adjt.cart.repository.CartRepository;
import br.com.fiap.postech.adjt.cart.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CartServiceImpl implements CartService {

    private final ItemsClient itemsClient;
    private final CartRepository cartRepository;

    public CartServiceImpl(ItemsClient itemsClient, CartRepository cartRepository) {
        this.itemsClient = itemsClient;
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart add(AddCartItemRequest request) {
        validateRequest(request);

        CartItem cartItem = new CartItem(request.itemId(), request.quantity());

        Cart cart = getCartByCustomerId(request.consumerId());
        cart.addItem(cartItem);

        Cart savedCart = cartRepository.save(cart);

        return savedCart;
    }

    @Override
    public Cart remove(RemoveCartItemRequest request) {
        Cart cart = getCartByCustomerIdIfExist(request.consumerId());
        cart.removeItem(request.itemId());

        Cart savedCart = cartRepository.save(cart);

        return savedCart;
    }

    @Override
    public Cart increment(IncrementCartItemRequest request) {
        Cart cart = getCartByCustomerIdIfExist(request.consumerId());
        cart.incrementItem(request.itemId());

        Cart savedCart = cartRepository.save(cart);

        return savedCart;
    }

    @Override
    public Cart findByCustomerId(UUID consumerId) {
        Cart cart = getCartByCustomerIdIfExist(consumerId);
        cart.checkIfCartIsEmpty();

        return cart;
    }

    @Override
    public Cart clear(UUID consumerId) {
        Cart cart = getCartByCustomerIdIfExist(consumerId);
        cart.clear();

        Cart savedCart = cartRepository.save(cart);

        return savedCart;
    }

    private void validateRequest(AddCartItemRequest request) {

        if (request.quantity() == null || request.quantity().isEmpty()
                || request.quantity().isBlank() || Integer.parseInt(request.quantity()) <= 0){
            throw new IllegalArgumentException("Invalid itemId quantity");
        }

        ResponseEntity<ItemResponse> itemResponse = itemsClient.findById(request.itemId());
        if (itemResponse.getStatusCode() != HttpStatus.OK || itemResponse.getBody() == null) {
            throw new NotFoundException("Invalid itemId does not exist");
        }

    }

    private Cart getCartByCustomerId(UUID consumerId) {
        return cartRepository.findByCustomerId(consumerId)
                .orElse(new Cart(consumerId));
    }

    private Cart getCartByCustomerIdIfExist(UUID uuid) {
        return cartRepository.findByCustomerId(uuid).orElseThrow(() -> new NotFoundException("Cart not found"));
    }
}
