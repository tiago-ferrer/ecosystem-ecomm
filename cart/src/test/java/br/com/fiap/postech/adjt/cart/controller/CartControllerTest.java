package br.com.fiap.postech.adjt.cart.controller;
import br.com.fiap.postech.adjt.cart.dto.AddOrRemoveItemRequest;
import br.com.fiap.postech.adjt.cart.dto.ItemRequest;
import br.com.fiap.postech.adjt.cart.exception.InvalidConsumerIdException;
import br.com.fiap.postech.adjt.cart.exception.InvalidItemIdException;
import br.com.fiap.postech.adjt.cart.exception.InvalidItemQuantityException;
import br.com.fiap.postech.adjt.cart.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

//    @Test
//    void getCart() {
//        ConsumerIdRequest request = new ConsumerIdRequest();
//        // Configure request as needed
//        CartResponse cartResponse = new CartResponse();
//        // Configure cartResponse as needed
//
//        when(cartService.getCart(request.consumerId())).thenReturn(cartResponse);
//
//        ResponseEntity<CartResponse> response = cartController.getCart(request);
//
//        assertEquals(ResponseEntity.ok(cartResponse), response);
//        verify(cartService).getCart(request.consumerId());
//    }
//
//    @Test
//    void deleteAllItens() {
//        ConsumerIdRequest request = new ConsumerIdRequest();
//        // Configure request as needed
//
//        ResponseEntity<String> response = cartController.deleteAllItens(request);
//
//        assertEquals(ResponseEntity.ok("Items removed from cart successfully"), response);
//        verify(cartService).deleteAllItens(request.consumerId());
//    }

}