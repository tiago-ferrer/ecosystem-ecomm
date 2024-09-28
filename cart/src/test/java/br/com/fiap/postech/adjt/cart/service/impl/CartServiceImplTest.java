package br.com.fiap.postech.adjt.cart.service.impl;

import br.com.fiap.postech.adjt.cart.clients.ItemsClient;
import br.com.fiap.postech.adjt.cart.controller.exception.InvalidConsumerIdFormatException;
import br.com.fiap.postech.adjt.cart.controller.exception.NotFoundException;
import br.com.fiap.postech.adjt.cart.model.dto.request.AddCartItemRequest;
import br.com.fiap.postech.adjt.cart.repository.CartRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.security.InvalidAlgorithmParameterException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

class CartServiceImplTest {

    @Mock
    private ItemsClient itemsClient;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void add_invalidCustomerId_shouldThrowInvalidCustomerIdException() {
        AddCartItemRequest request = new AddCartItemRequest("wrong-uuid", 1L, "10");

        InvalidConsumerIdFormatException invalidConsumerIdFormatException = assertThrows(
                InvalidConsumerIdFormatException.class,
                () -> cartService.add(request)
        );

        assertEquals(
                "Invalid consumerId format",
                invalidConsumerIdFormatException.getMessage()
        );
    }

    @Test
    void add_invalidQuantity_shouldThrowIllegalArgumentException() {
        AddCartItemRequest request = new AddCartItemRequest(UUID.randomUUID().toString(), 1L, null);

        IllegalArgumentException illegalArgumentException = assertThrows(
                IllegalArgumentException.class,
                () -> cartService.add(request)
        );

        assertEquals(
                "Invalid itemId quantity",
                illegalArgumentException.getMessage()
        );
    }

    @Test
    void add_invalidItemId_shouldThrowNotFoundException() {
        AddCartItemRequest request = new AddCartItemRequest(UUID.randomUUID().toString(), null, "10");

        when(itemsClient.findById(request.itemId())).thenReturn(ResponseEntity.badRequest().build());

        NotFoundException notFoundException = assertThrows(
                NotFoundException.class,
                () -> cartService.add(request)
        );

        assertEquals(
                "Invalid itemId does not exist",
                notFoundException.getMessage()
        );
    }
}