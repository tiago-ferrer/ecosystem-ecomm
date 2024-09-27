package br.com.fiap.postech.adjt.cart.service;

import br.com.fiap.postech.adjt.cart.entity.Cart;
import br.com.fiap.postech.adjt.cart.entity.Item;
import br.com.fiap.postech.adjt.cart.exception.EmptyCartException;
import br.com.fiap.postech.adjt.cart.exception.InvalidItemException;
import br.com.fiap.postech.adjt.cart.exception.InvalidItemQuantityException;
import br.com.fiap.postech.adjt.cart.repository.CartRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    AutoCloseable mock;

    @BeforeEach
    void setup() {
        this.mock = MockitoAnnotations.openMocks(this);
        this.cartService = new CartService(this.cartRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        this.mock.close();
    }

    @Nested
    class AddItem {

        @Test
        void shouldAddItem() {
            // Arrange
            UUID consumerId = UUID.randomUUID();
            Long itemId = 1L;
            Long quantity = 2L;

            Cart cart = new Cart();
            cart.setConsumerId(consumerId);

            Item item = new Item();
            item.setItemId(itemId);
            item.setQnt(quantity);
            item.setCart(cart);

            List<Item> items = new ArrayList<>();
            items.add(item);

            cart.setItems(items);

            when(cartRepository.findByConsumerId(any(UUID.class))).thenReturn(cart);
            when(cartRepository.save(any(Cart.class))).thenReturn(cart);

            // Act
            cartService.addItem(consumerId, itemId, quantity);

            // Assert
            verify(cartRepository, times(1)).save(any(Cart.class));
        }

        @Test
        void shouldThrowInvalidItemQuantityException() {
            // Arrange
            UUID consumerId = UUID.randomUUID();
            Long itemId = 1L;
            Long quantity = 0L;

            Cart cart = new Cart();
            cart.setConsumerId(consumerId);

            Item item = new Item();
            item.setItemId(itemId);
            item.setQnt(quantity);
            item.setCart(cart);

            List<Item> items = new ArrayList<>();
            items.add(item);

            cart.setItems(items);

            when(cartRepository.findByConsumerId(any(UUID.class))).thenReturn(cart);
            when(cartRepository.save(any(Cart.class))).thenReturn(cart);

            // Act & Assert
            assertThatThrownBy(() -> cartService.addItem(consumerId, itemId, quantity))
                    .isInstanceOf(InvalidItemQuantityException.class);
            verify(cartRepository, times(0)).save(any(Cart.class));
        }

        @Test
        void shouldThrowInvalidItemException() {
            // Arrange
            UUID consumerId = UUID.randomUUID();
            Long itemId = 2L;
            Long quantity = 1L;

            Cart cart = new Cart();
            cart.setConsumerId(consumerId);

            Item item = new Item();
            item.setItemId(1L);
            item.setQnt(2L);
            item.setCart(cart);

            List<Item> items = new ArrayList<>();
            items.add(item);

            cart.setItems(items);

            when(cartRepository.findByConsumerId(any(UUID.class))).thenReturn(cart);
            when(cartRepository.save(any(Cart.class))).thenReturn(cart);

            // Act & Assert
            assertThatThrownBy(() -> cartService.addItem(consumerId, itemId, quantity))
                    .isInstanceOf(InvalidItemException.class);
            verify(cartRepository, times(0)).save(any(Cart.class));
        }

    }

    @Nested
    class RemoveItem {

        @Test
        void shouldRemoveItem() {
            // Arrange
            UUID consumerId = UUID.randomUUID();
            Long itemId = 1L;
            Long quantity = 2L;

            Cart cart = new Cart();
            cart.setConsumerId(consumerId);

            Item item = new Item();
            item.setItemId(itemId);
            item.setQnt(quantity);
            item.setCart(cart);

            List<Item> items = new ArrayList<>();
            items.add(item);

            cart.setItems(items);

            when(cartRepository.findByConsumerId(any(UUID.class))).thenReturn(cart);
            when(cartRepository.save(any(Cart.class))).thenReturn(cart);

            // Act
            cartService.removeItem(consumerId, itemId);

            // Assert
            verify(cartRepository, times(1)).save(any(Cart.class));
        }

        @Test
        void shouldThrowEmptyCartException() {
            // Arrange
            UUID consumerId = UUID.randomUUID();
            Long itemId = 1L;

            Cart cart = new Cart();
            cart.setConsumerId(consumerId);
            cart.setItems(new ArrayList<>());

            when(cartRepository.findByConsumerId(any(UUID.class))).thenReturn(cart);

            assertThatThrownBy(() -> cartService.removeItem(consumerId, itemId))
                    .isInstanceOf(EmptyCartException.class);
        }

    }



    @Nested
    class IncrementItem {

        @Test
        void shouldIncrementItem() {
            // Arrange
            UUID consumerId = UUID.randomUUID();
            Long itemId = 1L;
            Long quantity = 0L;

            Cart cart = new Cart();
            cart.setConsumerId(consumerId);

            Item item = new Item();
            item.setItemId(itemId);
            item.setQnt(quantity);
            item.setCart(cart);

            List<Item> items = new ArrayList<>();
            items.add(item);

            cart.setItems(items);

            when(cartRepository.findByConsumerId(any(UUID.class))).thenReturn(cart);

            // Act
            cartService.incrementItem(consumerId, itemId);

            // Assert
            verify(cartRepository, times(1)).save(any(Cart.class));
        }

        @Test
        void shouldThrowInvalidItemException() {
            // Arrange
            UUID consumerId = UUID.randomUUID();
            Long itemId = 1L;

            Cart cart = new Cart();
            cart.setConsumerId(consumerId);

            Item item = new Item();
            item.setItemId(2L);
            item.setQnt(1L);
            item.setCart(cart);

            List<Item> items = new ArrayList<>();
            items.add(item);

            cart.setItems(items);

            when(cartRepository.findByConsumerId(any(UUID.class))).thenReturn(cart);

            assertThatThrownBy(() -> cartService.incrementItem(consumerId, itemId))
                    .isInstanceOf(InvalidItemException.class);
        }

    }


    @Nested
    class FindCart {

        @Test
        void shouldFindCart() {
            // Arrange
            UUID consumerId = UUID.randomUUID();
            Long itemId = 1L;
            Long quantity = 0L;

            Cart cart = new Cart();
            cart.setConsumerId(consumerId);

            Item item = new Item();
            item.setItemId(itemId);
            item.setQnt(quantity);
            item.setCart(cart);

            List<Item> items = new ArrayList<>();
            items.add(item);

            cart.setItems(items);

            when(cartRepository.findByConsumerId(any(UUID.class))).thenReturn(cart);

            // Act
            Cart foundCart = cartService.findCart(consumerId);

            // Assert
            assertThat(foundCart)
                    .isNotNull()
                    .isInstanceOf(Cart.class)
                    .isEqualTo(cart);
        }

        @Test
        void shouldThrowEmptyCartException() {
            // Arrange
            UUID consumerId = UUID.randomUUID();

            Cart cart = new Cart();
            cart.setConsumerId(consumerId);
            cart.setItems(new ArrayList<>());

            when(cartRepository.findByConsumerId(any(UUID.class))).thenReturn(cart);

            assertThatThrownBy(() -> cartService.findCart(consumerId))
                    .isInstanceOf(EmptyCartException.class);
        }

    }

    @Test
    void shouldRemoveAllItems() {
        // Arrange
        UUID consumerId = UUID.randomUUID();
        Long itemId = 1L;
        Long quantity = 0L;

        Cart cart = new Cart();
        cart.setConsumerId(consumerId);

        Item item = new Item();
        item.setItemId(itemId);
        item.setQnt(quantity);
        item.setCart(cart);

        List<Item> items = new ArrayList<>();
        items.add(item);

        cart.setItems(items);

        when(cartRepository.findByConsumerId(any(UUID.class))).thenReturn(cart);

        // Act
        cartService.removeAllItems(consumerId);

        // Assert
        assertThat(cart.getItems()).isEmpty();
        verify(cartRepository, times(1)).save(cart);
    }

}
