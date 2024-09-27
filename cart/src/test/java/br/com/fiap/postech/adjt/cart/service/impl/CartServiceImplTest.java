package br.com.fiap.postech.adjt.cart.service.impl;

import br.com.fiap.postech.adjt.cart.dto.CartDto;
import br.com.fiap.postech.adjt.cart.dto.CartResponseRecord;
import br.com.fiap.postech.adjt.cart.dto.Messages;
import br.com.fiap.postech.adjt.cart.exceptions.CartException;
import br.com.fiap.postech.adjt.cart.model.Item;
import br.com.fiap.postech.adjt.cart.repository.ItemRepository;
import br.com.fiap.postech.adjt.cart.service.ProductService;
import br.com.fiap.postech.adjt.cart.test.utils.TestUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ProductService productService;


    @Nested
    class TestsAddItem {

        @Test
        void testAddItemSuccess() {
            when(productService.findProductById(Mockito.anyLong()))
                    .thenReturn(TestUtils.buildProductDto());
            when(itemRepository.findById(Mockito.any()))
                    .thenReturn(Optional.empty());
            ResponseEntity<CartResponseRecord> resp = cartService.addItem(TestUtils.buildCartRequest());
            verify(itemRepository, times(1)).save(Mockito.any());
            assertNotNull(resp.getBody());
            assertEquals(Messages.ADD_ITEM_SUCCESS, resp.getBody().message());
            assertEquals(HttpStatus.OK, resp.getStatusCode());
        }

        @Test
        void testAddItemNoSaveWhenExist() {
            when(productService.findProductById(Mockito.anyLong()))
                    .thenReturn(TestUtils.buildProductDto());
            when(itemRepository.findById(Mockito.any()))
                    .thenReturn(Optional.of(TestUtils.buildItem()));
            ResponseEntity<CartResponseRecord> resp = cartService.addItem(TestUtils.buildCartRequest());
            verify(itemRepository, never()).save(Mockito.any());
            assertNotNull(resp.getBody());
            assertEquals(Messages.ADD_ITEM_SUCCESS, resp.getBody().message());
            assertEquals(HttpStatus.OK, resp.getStatusCode());
        }

        @Test
        void testAddItemException() {
            when(productService.findProductById(Mockito.anyLong()))
                    .thenThrow(CartException.class);
            assertThrows(CartException.class, () ->
                    cartService.addItem(TestUtils.buildCartRequest()));
        }

    }


    @Nested
    class TestsDecrementItem {

        @Test
        void testDecrementItemSuccess() {
            when(itemRepository.findById(Mockito.any())).thenReturn(Optional.of(TestUtils.buildItem()));
            ResponseEntity<CartResponseRecord> resp = cartService.decrementItem(TestUtils.buildCartRequest());
            verify(itemRepository, times(1)).save(Mockito.any());
            verify(itemRepository, never()).deleteById(Mockito.any());
            assertNotNull(resp.getBody());
            assertEquals(Messages.DEC_ITEM_SUCCESS, resp.getBody().message());
            assertEquals(HttpStatus.OK, resp.getStatusCode());
        }

        @Test
        void testDecrementItemWhenQuantityZero() {
            Item item = TestUtils.buildItem();
            item.setQnt(1);
            when(itemRepository.findById(Mockito.any())).thenReturn(Optional.of(item));
            ResponseEntity<CartResponseRecord> resp = cartService.decrementItem(TestUtils.buildCartRequest());
            verify(itemRepository, never()).save(Mockito.any());
            verify(itemRepository, times(1)).deleteById(Mockito.any());
            assertNotNull(resp.getBody());
            assertEquals(Messages.DEC_ITEM_SUCCESS, resp.getBody().message());
            assertEquals(HttpStatus.OK, resp.getStatusCode());
        }

        @Test
        void testDecrementItemNotFoundItem() {
            when(itemRepository.findById(Mockito.any())).thenReturn(Optional.empty());
            ResponseEntity<CartResponseRecord> resp = cartService.decrementItem(TestUtils.buildCartRequest());
            verify(itemRepository, never()).save(Mockito.any());
            verify(itemRepository, never()).deleteById(Mockito.any());
            assertNotNull(resp.getBody());
            assertEquals(Messages.DEC_ITEM_SUCCESS, resp.getBody().message());
            assertEquals(HttpStatus.OK, resp.getStatusCode());
        }

    }

    @Nested
    class TestsIncrementItem {

        @Test
        void testIncrementItemSuccess() {
            when(itemRepository.findById(Mockito.any())).thenReturn(Optional.of(TestUtils.buildItem()));
            ResponseEntity<CartResponseRecord> resp = cartService.incrementItem(TestUtils.buildCartRequest());
            verify(itemRepository, times(1)).save(Mockito.any());
            assertNotNull(resp.getBody());
            assertEquals(Messages.INC_ITEM_SUCCESS, resp.getBody().message());
            assertEquals(HttpStatus.OK, resp.getStatusCode());
        }

        @Test
        void testIncrementItemException() {
            when(itemRepository.findById(Mockito.any())).thenReturn(Optional.empty());
            String msgError = assertThrows(CartException.class, () ->
                    cartService.incrementItem(TestUtils.buildCartRequest())).getMessage();
            assertEquals(Messages.ITEM_NOT_FOUND, msgError);
        }

    }


    @Nested
    class TestsFindCartByConsumerId {

        @Test
        void testFindCartByConsumerIdSuccess() {
            when(itemRepository.findAllByConsumerId(Mockito.any(UUID.class)))
                    .thenReturn(List.of(TestUtils.buildItem()));
            ResponseEntity<CartDto> resp = cartService.findCartByConsumerId(TestUtils.buildCartRequest());
            assertNotNull(resp.getBody());
            assertEquals(HttpStatus.OK, resp.getStatusCode());
            assertEquals(1, resp.getBody().items().size());
        }

        @Test
        void testFindCartByConsumerWhenItemsNull() {
            when(itemRepository.findAllByConsumerId(Mockito.any(UUID.class))).thenReturn(null);
            String msgError = assertThrows(CartException.class, () ->
                    cartService.findCartByConsumerId(TestUtils.buildCartRequest())).getMessage();
            assertEquals(Messages.CART_EMPTY, msgError);
        }

        @Test
        void testFindCartByConsumerWhenItemsIsEmpty() {
            when(itemRepository.findAllByConsumerId(Mockito.any(UUID.class)))
                    .thenReturn(List.of());
            String msgError = assertThrows(CartException.class, () ->
                    cartService.findCartByConsumerId(TestUtils.buildCartRequest())).getMessage();
            assertEquals(Messages.CART_EMPTY, msgError);
        }

    }


    @Nested
    class TestsDeleteCartByConsumerId {

        @Test
        void testDeleteCartByConsumerIdSuccess() {
            ResponseEntity<CartResponseRecord> resp = cartService.deleteCartByConsumerId(TestUtils.buildCartRequest());
            verify(itemRepository, times(1)).deleteByConsumerId(Mockito.any());
            assertNotNull(resp.getBody());
            assertEquals(HttpStatus.OK, resp.getStatusCode());
            assertEquals(Messages.CART_CLEAR_ITEMS, resp.getBody().message());
        }

        @Test
        void testFindCartByConsumerWhenItemsNull() {
            doThrow(RuntimeException.class).when(itemRepository).deleteByConsumerId(Mockito.any());
            assertThrows(RuntimeException.class, () ->
                    cartService.deleteCartByConsumerId(TestUtils.buildCartRequest()));
        }

    }

}
