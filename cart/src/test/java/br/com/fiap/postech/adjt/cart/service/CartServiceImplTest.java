package br.com.fiap.postech.adjt.cart.service;

import br.com.fiap.postech.adjt.cart.dto.AddOrRemoveItemRequest;
import br.com.fiap.postech.adjt.cart.dto.CartResponse;
import br.com.fiap.postech.adjt.cart.dto.ItemRequest;
import br.com.fiap.postech.adjt.cart.dto.ProductDto;
import br.com.fiap.postech.adjt.cart.exception.EmptyCartException;
import br.com.fiap.postech.adjt.cart.exception.InvalidConsumerIdException;
import br.com.fiap.postech.adjt.cart.exception.InvalidItemIdException;
import br.com.fiap.postech.adjt.cart.exception.InvalidItemQuantityException;
import br.com.fiap.postech.adjt.cart.feign.ProductClient;
import br.com.fiap.postech.adjt.cart.model.Cart;
import br.com.fiap.postech.adjt.cart.model.Item;
import br.com.fiap.postech.adjt.cart.repository.CartItemRepository;
import br.com.fiap.postech.adjt.cart.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductClient productClient;

    @InjectMocks
    public CartServiceImpl cartService;

    private UUID consumerId;
    private Long itemId;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        consumerId = UUID.randomUUID();
        itemId = 1L;
        productDto = new ProductDto(itemId, "Product", BigDecimal.valueOf(100.0), "Description", "Category", "Image");
    }

    @Test
    void createCartItem() {

        ItemRequest request = new ItemRequest(consumerId.toString(), itemId, 2);

        when(productClient.getProductById(itemId)).thenReturn(productDto);
        when(cartRepository.findByConsumerId(consumerId)).thenReturn(Optional.empty());

        cartService.createCartItem(request);

        verify(cartItemRepository).save(any(Item.class));
    }

    @Test
    void createCartItemInvalidConsumerIdFormat() {

        ItemRequest request = new ItemRequest("invalid-uuid", itemId, 2);

        Exception exception = assertThrows(InvalidConsumerIdException.class, () -> {
            cartService.createCartItem(request);
        });

        assertEquals("Invalid consumerId format", exception.getMessage());
    }

    @Test
    void createCartItemInvalidItemId() {

        ItemRequest request = new ItemRequest(consumerId.toString(), 999L, 2);

        when(productClient.getProductById(999L)).thenReturn(null);
        Exception exception = assertThrows(InvalidItemIdException.class, () -> {
            cartService.createCartItem(request);
        });

        assertEquals("Invalid itemId does not exist", exception.getMessage());
    }

    @Test
    void createCartItemInvalidItemQuantity() {

        ProductDto mockProduct = new ProductDto(2L, "Sample Product", new BigDecimal("20.00"),
                "Sample Description", "Category A", "image-url");

        when(productClient.getProductById(2L)).thenReturn(mockProduct);

        ItemRequest request = new ItemRequest(consumerId.toString(), 2L, -1);

        Exception exception = assertThrows(InvalidItemQuantityException.class, () -> {
            cartService.createCartItem(request);
        });

        assertEquals("Invalid item quantity", exception.getMessage());
    }

    @Test
    void createCartItemInvalidItemNull() {

        ProductDto mockProduct = new ProductDto(null, "Sample Product", new BigDecimal("20.00"),
                "Sample Description", "Category A", "image-url");

        when(productClient.getProductById(null)).thenReturn(mockProduct);

        ItemRequest request = new ItemRequest(consumerId.toString(), null, 1);

        Exception exception = assertThrows(InvalidItemIdException.class, () -> {
            cartService.createCartItem(request);
        });

        assertEquals("Invalid itemId does not exist", exception.getMessage());
    }

    @Test
    void removeItemFromCart() {

        UUID consumerId = UUID.fromString("153e23c8-302e-4fec-b9c4-72b8f74ad102");
        AddOrRemoveItemRequest request = new AddOrRemoveItemRequest(consumerId.toString(), itemId);

        Item item = new Item();
        item.setItemId(itemId);
        item.setConsumerId(consumerId);
        item.setQuantity(2);
        item.setPrice(productDto.price());

        when(cartItemRepository.findByConsumerIdAndItemId(consumerId, itemId)).thenReturn(Optional.of(item));
       cartService.removeItemFromCart(request);

        assertEquals(1, item.getQuantity());
        verify(cartItemRepository, never()).delete(item);
    }

    @Test
    void removeItemFromCartinvalidConsumerIdFormat() {

        String invalidConsumerId = "invalid-uuid-format";
        Long itemId = 1L;
        AddOrRemoveItemRequest request = new AddOrRemoveItemRequest(invalidConsumerId, itemId);

        Exception exception = assertThrows(InvalidConsumerIdException.class, () -> {
            cartService.removeItemFromCart(request);
        });

        assertEquals("Invalid consumerId format", exception.getMessage());
    }


    @Test
    void addItemToCart() {

        UUID consumerId = UUID.fromString("153e23c8-302e-4fec-b9c4-72b8f74ad102");
        AddOrRemoveItemRequest request = new AddOrRemoveItemRequest(consumerId.toString(), itemId);

        Item item = new Item();
        item.setItemId(itemId);
        item.setConsumerId(consumerId);
        item.setQuantity(3);
        item.setPrice(productDto.price());

        when(cartItemRepository.findByConsumerIdAndItemId(consumerId, itemId)).thenReturn(Optional.of(item));

        cartService.addItemToCart(request);

        verify(cartItemRepository).save(item);
        assertEquals(4, item.getQuantity());
    }

    @Test
    void addItemToCartinvalidConsumerIdFormat() {

        String invalidConsumerId = "invalid-uuid-format";
        Long itemId = 1L;
        AddOrRemoveItemRequest request = new AddOrRemoveItemRequest(invalidConsumerId, itemId);

        Exception exception = assertThrows(InvalidConsumerIdException.class, () -> {
            cartService.addItemToCart(request);
        });

        assertEquals("Invalid consumerId format", exception.getMessage());
    }

    @Test
    void addItemToCartinvalidItemId() {

        UUID consumerId = UUID.fromString("153e23c8-302e-4fec-b9c4-72b8f74ad102");
        Long invalidItemId = -1L;
        AddOrRemoveItemRequest request = new AddOrRemoveItemRequest(consumerId.toString(), invalidItemId);

        Exception exception = assertThrows(InvalidItemIdException.class, () -> {
            cartService.addItemToCart(request);
        });

        assertEquals("Invalid itemId", exception.getMessage());
    }

    @Test
    void getCart() {

        Cart cart = new Cart();
        cart.setCartId(1L);
        cart.setConsumerId(consumerId);

        Item item = new Item();
        item.setItemId(itemId);
        item.setConsumerId(consumerId);
        item.setQuantity(3);
        item.setPrice(productDto.price());

        cart.setItems(List.of(item));

        when(cartRepository.findByConsumerId(consumerId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCart_CartId(cart.getCartId())).thenReturn(List.of(item));

        CartResponse response = cartService.getCart(consumerId.toString());

        assertNotNull(response);
        assertEquals(1, response.items().size());
    }

    @Test
    void getCartemptyCart() {

        UUID consumerId = UUID.fromString("153e23c8-302e-4fec-b9c4-72b8f74ad102");

        when(cartRepository.findByConsumerId(consumerId)).thenReturn(Optional.of(new Cart()));

        Exception exception = assertThrows(EmptyCartException.class, () -> {
            cartService.getCart(consumerId.toString());
        });

        assertEquals("Empty cart", exception.getMessage());
    }

    @Test
    void getCartinvalidConsumerIdFormat() {

        String invalidConsumerId = "invalid-uuid-format";

        Exception exception = assertThrows(InvalidConsumerIdException.class, () -> {
            cartService.getCart(invalidConsumerId);
        });

        assertEquals("Invalid consumerId format", exception.getMessage());
    }


    @Test
    void deleteAllItens() {

        Cart cart = new Cart();
        cart.setCartId(1L);
        cart.setConsumerId(consumerId);

        Item item = new Item();
        item.setItemId(itemId);
        item.setConsumerId(consumerId);
        item.setQuantity(3);
        item.setPrice(productDto.price());

        cart.setItems(List.of(item));

        when(cartRepository.findByConsumerId(consumerId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCart_CartId(cart.getCartId())).thenReturn(List.of(item));

        cartService.deleteAllItens(consumerId.toString());

        verify(cartItemRepository).deleteAll(anyList());
        verify(cartItemRepository).findByCart_CartId(cart.getCartId());
    }

    @Test
    void deleteAllItensinvalidConsumerIdFormat() {

        String invalidConsumerId = "invalid-uuid-format";

        Exception exception = assertThrows(InvalidConsumerIdException.class, () -> {
            cartService.deleteAllItens(invalidConsumerId);
        });

        assertEquals("Invalid consumerId format", exception.getMessage());
    }


}

