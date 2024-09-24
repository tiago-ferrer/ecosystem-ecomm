package br.com.fiap.postech.adjt.cart.controller;
import br.com.fiap.postech.adjt.cart.dto.*;
import br.com.fiap.postech.adjt.cart.exception.EmptyCartException;
import br.com.fiap.postech.adjt.cart.exception.InvalidConsumerIdException;
import br.com.fiap.postech.adjt.cart.exception.InvalidItemIdException;
import br.com.fiap.postech.adjt.cart.exception.InvalidItemQuantityException;
import br.com.fiap.postech.adjt.cart.model.Cart;
import br.com.fiap.postech.adjt.cart.model.Item;
import br.com.fiap.postech.adjt.cart.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private CartService cartService;

    private UUID consumerId;
    private Long itemId;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productDto = new ProductDto(itemId, "Product", BigDecimal.valueOf(100.0), "Description", "Category", "Image");
    }


    @Test
    void createCartItem() {

        String consumerId = "153e23c8-302e-4fec-b9c4-72b8f74ad102";
        Long itemId = 1L;
        int quantity = 2;
        ItemRequest itemRequest = new ItemRequest(consumerId, itemId, quantity);

        ResponseEntity<String> response = cartController.createCartItem(itemRequest);

        assertEquals(ResponseEntity.ok("Item added to cart successfully"), response);

        verify(cartService).createCartItem(itemRequest);
    }

    @Test
    void createCartItem_InvalidConsumerIdFormat() {

        String invalidConsumerId = "invalid-consumer-id";
        Long itemId = 1L;
        int quantity = 2;
        ItemRequest itemRequest = new ItemRequest(invalidConsumerId, itemId, quantity);

        Exception exception = assertThrows(InvalidConsumerIdException.class, () -> {
            cartController.createCartItem(itemRequest);
        });

        assertEquals("Invalid consumerId format", exception.getMessage());

        verify(cartService, never()).createCartItem(any());
    }

    @Test
    void createCartItem_InvalidItemIdDoesNotExist() {
        String validConsumerId = "153e23c8-302e-4fec-b9c4-72b8f74ad102";
        Long invalidItemId = -1L;
        int quantity = 2;
        ItemRequest itemRequest = new ItemRequest(validConsumerId, invalidItemId, quantity);

        doThrow(new InvalidItemIdException("Invalid itemId does not exist")).when(cartService).createCartItem(any());

        Exception exception = assertThrows(InvalidItemIdException.class, () -> {
            cartController.createCartItem(itemRequest);
        });

        assertEquals("Invalid itemId does not exist", exception.getMessage());

        verify(cartService).createCartItem(itemRequest);
    }

    @Test
    void createCartItem_InvalidItemQuantity() {
        String validConsumerId = "153e23c8-302e-4fec-b9c4-72b8f74ad102";
        Long validItemId = 1L;
        int invalidQuantity = 0;
        ItemRequest itemRequest = new ItemRequest(validConsumerId, validItemId, invalidQuantity);

        doThrow(new InvalidItemQuantityException("Invalid itemId quantity")).when(cartService).createCartItem(any());

        Exception exception = assertThrows(InvalidItemQuantityException.class, () -> {
            cartController.createCartItem(itemRequest);
        });

        assertEquals("Invalid itemId quantity", exception.getMessage());

        verify(cartService).createCartItem(itemRequest);
    }

    @Test
    void removeItemFromCart() {
        AddOrRemoveItemRequest request = new AddOrRemoveItemRequest("152e23c8-302e-4fec-b9c4-72b8f74ad102",1L);

        ResponseEntity<String> response = cartController.removeItemFromCart(request);

        assertEquals(ResponseEntity.ok("Item removed from cart successfully"), response);
        verify(cartService).removeItemFromCart(request);
    }

    @Test
    void removeItemFromCart_InvalidConsumerIdFormat() {
        String invalidConsumerId = "invalid-consumer-id";
        Long validItemId = 1L;
        AddOrRemoveItemRequest request = new AddOrRemoveItemRequest(invalidConsumerId, validItemId);

        doThrow(new InvalidConsumerIdException("Invalid consumerId format"))
                .when(cartService).removeItemFromCart(any());

        Exception exception = assertThrows(InvalidConsumerIdException.class, () -> {
            cartController.removeItemFromCart(request);
        });

        assertEquals("Invalid consumerId format", exception.getMessage());

        verify(cartService).removeItemFromCart(any());
    }


    @Test
    void addItemFromCart() {
        UUID consumerId = UUID.fromString("153e23c8-302e-4fec-b9c4-72b8f74ad102");
        Long invalidItemId = -1L;
        AddOrRemoveItemRequest request = new AddOrRemoveItemRequest(consumerId.toString(), invalidItemId);

        ResponseEntity<String> response = cartController.addItemFromCart(request);

        assertEquals(ResponseEntity.ok("Item added from cart successfully"), response);
        verify(cartService).addItemToCart(request);
    }

    @Test
    void addItemFromCart_InvalidConsumerIdFormat() {

        String invalidConsumerId = "invalid-consumer-id";
        Long validItemId = 1L;
        AddOrRemoveItemRequest request = new AddOrRemoveItemRequest(invalidConsumerId, validItemId);

        ResponseEntity<String> response = cartController.addItemFromCart(request);

        assertEquals(ResponseEntity.badRequest().body("Invalid consumerId format"), response);

        verify(cartService, never()).addItemToCart(any());
    }

    @Test
    void addItemFromCart_InvalidItemId() {
        String validConsumerId = "153e23c8-a02e-4fec-b9c4-72b8f74ad102";
        Long invalidItemId = -0L;
        AddOrRemoveItemRequest request = new AddOrRemoveItemRequest(validConsumerId, invalidItemId);

        ResponseEntity<String> response = cartController.addItemFromCart(request);

        assertEquals(ResponseEntity.badRequest().body("Invalid itemId"), response);
        verify(cartService, never()).addItemToCart(any());
    }

    @Test
    void getCart() {

        UUID consumerId = UUID.fromString("153e23c8-a02e-4fec-b9c4-72b8f74ad102");

        Cart cart = new Cart();
        cart.setCartId(1L);
        cart.setConsumerId(consumerId);

        Item item = new Item();
        item.setItemId(1L);
        item.setConsumerId(consumerId);
        item.setQuantity(3);
        item.setPrice(productDto.price());

        cart.setItems(List.of(item));

        List<ItemResponse> itemResponses = List.of(new ItemResponse(item.getItemId(), item.getQuantity()));
        CartResponse cartResponse = new CartResponse(itemResponses);

        when(cartService.getCart(consumerId.toString())).thenReturn(cartResponse);

        ConsumerIdRequest request = new ConsumerIdRequest(consumerId.toString());

        ResponseEntity<CartResponse> response = cartController.getCart(request);

        assertEquals(ResponseEntity.ok(cartResponse), response);
        verify(cartService).getCart(consumerId.toString());
    }

    @Test
    void getCart_EmptyCart() {

        UUID consumerId = UUID.fromString("153e23c8-a02e-4fec-b9c4-72b8f74ad102");

        when(cartService.getCart(consumerId.toString())).thenThrow(new EmptyCartException("Empty cart"));

        ConsumerIdRequest request = new ConsumerIdRequest(consumerId.toString());

        Exception exception = assertThrows(EmptyCartException.class, () -> {
            cartController.getCart(request);
        });

        assertEquals("Empty cart", exception.getMessage());

        verify(cartService).getCart(consumerId.toString());
    }

    @Test
    void getCart_InvalidConsumerIdFormat() {

        String invalidConsumerId = "invalid-consumer-id";

        when(cartService.getCart(invalidConsumerId)).thenThrow(new InvalidConsumerIdException("Invalid consumerId format"));

        ConsumerIdRequest request = new ConsumerIdRequest(invalidConsumerId);

        Exception exception = assertThrows(InvalidConsumerIdException.class, () -> {
            cartController.getCart(request);
        });

        assertEquals("Invalid consumerId format", exception.getMessage());

        verify(cartService).getCart(invalidConsumerId);
    }


    @Test
    void deleteAllItens() {

        String consumerId = "153e23c8-a02e-4fec-b9c4-72b8f74ad102";
        ConsumerIdRequest request = new ConsumerIdRequest(consumerId);

        doNothing().when(cartService).deleteAllItens(consumerId);

        ResponseEntity<String> response = cartController.deleteAllItens(request);

        assertEquals(ResponseEntity.ok("Items removed from cart successfully"), response);

        verify(cartService).deleteAllItens(consumerId);
    }


    @Test
    void deleteAllItens_InvalidConsumerIdFormat() {

        String invalidConsumerId = "invalid-consumer-id";
        ConsumerIdRequest request = new ConsumerIdRequest(invalidConsumerId);

        Exception exception = assertThrows(InvalidConsumerIdException.class, () -> {
            cartController.deleteAllItens(request);
        });

        assertEquals("Invalid consumerId format", exception.getMessage());

        verify(cartService, never()).deleteAllItens(any());
    }


}