package br.com.fiap.postech.adjt.cart.controller;

import br.com.fiap.postech.adjt.cart.dto.*;
import br.com.fiap.postech.adjt.cart.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        reset(cartService);
    }

    @Test
    void shouldCreateCartItem() throws Exception {
        String consumerId = "153e23c8-302e-4fec-b9c4-72b8f74ad102";
        Long itemId = 1L;
        int quantity = 2;
        ItemRequest itemRequest = new ItemRequest(consumerId, itemId, quantity);

        mockMvc.perform(MockMvcRequestBuilders.post("/cart/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new MessageResponse("Item added to cart successfully"))));

        verify(cartService, times(1)).createCartItem(any(ItemRequest.class));
    }

    @Test
    void shouldAddItemFromCart() throws Exception {

        String consumerId = "153e23c8-302e-4fec-b9c4-72b8f74ad102";
        Long itemId = 1L;

        AddOrRemoveItemRequest request = new AddOrRemoveItemRequest(consumerId, itemId);

        mockMvc.perform(MockMvcRequestBuilders.put("/cart/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new MessageResponse("Item added from cart successfully"))));

        verify(cartService, times(1)).addItemToCart(any(AddOrRemoveItemRequest.class));
    }

    @Test
    void shouldRemoveItemFromCart() throws Exception {

        String consumerId = "153e23c8-302e-4fec-b9c4-72b8f74ad102";
        Long itemId = 1L;
        AddOrRemoveItemRequest removeRequest = new AddOrRemoveItemRequest(consumerId, itemId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/cart/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(removeRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new MessageResponse("Item removed from cart successfully"))));

        verify(cartService, times(1)).removeItemFromCart(any(AddOrRemoveItemRequest.class));
    }

    @Test
    void shouldGetCart() throws Exception {
        UUID consumerId = UUID.fromString("153e23c8-a02e-4fec-b9c4-72b8f74ad102");

        List<ItemResponse> itemResponses = List.of(new ItemResponse(1L, 3));
        CartResponse cartResponse = new CartResponse(itemResponses);

        when(cartService.getCart(anyString())).thenReturn(cartResponse);

        ConsumerIdRequest consumerIdRequest = new ConsumerIdRequest(consumerId.toString());

        mockMvc.perform(MockMvcRequestBuilders.get("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(consumerIdRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(cartResponse)));

        verify(cartService, times(1)).getCart(anyString());
    }

    @Test
    void shouldDeleteAllItems() throws Exception {
        UUID consumerId = UUID.fromString("153e23c8-a02e-4fec-b9c4-72b8f74ad102");

        ConsumerIdRequest consumerIdRequest = new ConsumerIdRequest(consumerId.toString());

        mockMvc.perform(MockMvcRequestBuilders.delete("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(consumerIdRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new MessageResponse("Items removed from cart successfully"))));

        verify(cartService, times(1)).deleteAllItens(anyString());
    }
}
