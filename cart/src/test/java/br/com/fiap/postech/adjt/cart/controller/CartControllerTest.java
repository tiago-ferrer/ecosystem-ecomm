package br.com.fiap.postech.adjt.cart.controller;

import br.com.fiap.postech.adjt.cart.controller.request.AddItemRequest;
import br.com.fiap.postech.adjt.cart.controller.request.ChangeItemRequest;
import br.com.fiap.postech.adjt.cart.controller.request.FindCartRequest;
import br.com.fiap.postech.adjt.cart.controller.request.RemoveAllItemsRequest;
import br.com.fiap.postech.adjt.cart.entity.Cart;
import br.com.fiap.postech.adjt.cart.entity.Item;
import br.com.fiap.postech.adjt.cart.service.CartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CartControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    AutoCloseable mock;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
        CartController cartController = new CartController(cartService);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController)
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }, "/*")
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Nested
    class AddItem {

        @Test
        void shouldAddItem() throws Exception {
            // Arrange
            AddItemRequest addItemRequest = buildAddItemRequest();

            // Act & Assert
            mockMvc.perform(
                    post("/items")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(addItemRequest)))
                    .andExpect(status().is2xxSuccessful()
            );
            verify(cartService, times(1)).addItem(any(UUID.class), any(Long.class), any(Long.class));
        }

        @Test
        void shouldThrowInvalidUUIDException() throws Exception {
            // Arrange
            AddItemRequest addItemRequest = buildAddItemRequestWithInvalidUUID();

            // Act & Assert
            mockMvc.perform(
                            post("/items")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(addItemRequest)))
                    .andExpect(status().is4xxClientError());
            verify(cartService, never()).addItem(any(UUID.class), any(Long.class), any(Long.class));
        }

        @Test
        void shouldThrowIllegalArgumentException() throws Exception {
            // Arrange
            AddItemRequest addItemRequest = buildAddItemRequestWithInvalidUUID();

            // Act & Assert
            mockMvc.perform(
                            post("/items")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(addItemRequest)))
                    .andExpect(status().is4xxClientError());
            verify(cartService, never()).addItem(any(UUID.class), any(Long.class), any(Long.class));
        }

    }

    @Nested
    class RemoveItem {

        @Test
        void shouldRemoveItem() throws Exception {
            // Arrange
            ChangeItemRequest changeItemRequest = buildChangeItemRequest();

            // Act & Assert
            mockMvc.perform(
                    delete("/item")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(changeItemRequest)))
                    .andExpect(status().is2xxSuccessful());
            verify(cartService, times(1)).removeItem(any(UUID.class), any(Long.class));
        }

        @Test
        void shouldThrowInvalidUUIDException() throws Exception {
            // Arrange
            ChangeItemRequest changeItemRequest = new ChangeItemRequest("1234", 1L);

            // Act & Assert
            mockMvc.perform(
                            delete("/item")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(changeItemRequest)))
                    .andExpect(status().is4xxClientError());
            verify(cartService, never()).removeItem(any(UUID.class), any(Long.class));
        }

    }

    @Nested
    class IncrementItem {

        @Test
        void shouldIncrementItem() throws Exception {
            // Arrange
            ChangeItemRequest changeItemRequest = buildChangeItemRequest();

            // Act & Assert
            mockMvc.perform(
                            put("/item")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(changeItemRequest)))
                    .andExpect(status().is2xxSuccessful());
            verify(cartService, times(1)).incrementItem(any(UUID.class), any(Long.class));
        }

        @Test
        void shouldThrowInvalidUUIDException() throws Exception {
            // Arrange
            ChangeItemRequest changeItemRequest = new ChangeItemRequest("1234", 1L);

            // Act & Assert
            mockMvc.perform(
                            put("/item")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(changeItemRequest)))
                    .andExpect(status().is4xxClientError());
            verify(cartService, never()).incrementItem(any(UUID.class), any(Long.class));
        }

    }

    @Nested
    class FindCart {

        @Test
        void shouldFindCart() throws Exception {
            // Arrange
            FindCartRequest findCartRequest = buildFindCartRequest();
            Cart cart = new Cart();
            cart.setConsumerId(UUID.fromString(findCartRequest.consumerId()));

            Item item = new Item();
            item.setItemId(1L);
            item.setQnt(2L);
            item.setCart(cart);

            List<Item> items = new ArrayList<>();
            items.add(item);

            cart.setItems(items);
            when(cartService.findCart(any(UUID.class))).thenReturn(cart);

            // Act & Assert
            mockMvc.perform(
                            get("/")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(findCartRequest)))
                    .andExpect(status().is2xxSuccessful());
            verify(cartService, times(1)).findCart(any(UUID.class));
        }

        @Test
        void shouldThrowInvalidUUIDException() throws Exception {
            // Arrange
            FindCartRequest findCartRequest = new FindCartRequest("1234");

            // Act & Assert
            mockMvc.perform(
                            get("/")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(findCartRequest)))
                    .andExpect(status().is4xxClientError());
            verify(cartService, never()).findCart(any(UUID.class));
        }

    }

    @Nested
    class RemoveAllItems {

        @Test
        void shouldRemoveAllItems() throws Exception {
            // Arrange
            RemoveAllItemsRequest removeAllItemsRequest = buildRemoveAllItemsRequest();

            // Act & Assert
            mockMvc.perform(
                            delete("/")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(removeAllItemsRequest)))
                    .andExpect(status().is2xxSuccessful());
            verify(cartService, times(1)).removeAllItems(any(UUID.class));
        }

        @Test
        void shouldThrowInvalidUUIDException() throws Exception {
            // Arrange
            RemoveAllItemsRequest removeAllItemsRequest = new RemoveAllItemsRequest("1234");

            // Act & Assert
            mockMvc.perform(
                            delete("/")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(removeAllItemsRequest)))
                    .andExpect(status().is4xxClientError());
            verify(cartService, never()).removeAllItems(any(UUID.class));
        }

    }

    private AddItemRequest buildAddItemRequest() {
        return new AddItemRequest(
            "2012db3a-1ad7-4446-bbac-844977e8649b",
                "2",
                "1"
        );
    }

    private AddItemRequest buildAddItemRequestWithInvalidUUID() {
        return new AddItemRequest(
                "1234",
                "2",
                "1"
        );
    }

    private ChangeItemRequest buildChangeItemRequest() {
        return new ChangeItemRequest(
                "2012db3a-1ad7-4446-bbac-844977e8649b",
                2L
        );
    }

    private FindCartRequest buildFindCartRequest() {
        return new FindCartRequest(
                "2012db3a-1ad7-4446-bbac-844977e8649b"
        );
    }

    private RemoveAllItemsRequest buildRemoveAllItemsRequest() {
        return new RemoveAllItemsRequest(
                "2012db3a-1ad7-4446-bbac-844977e8649b"
        );
    }

    private String asJsonString(final Object object) throws JsonProcessingException {
        return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(object);
    }

}
