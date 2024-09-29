package br.com.fiap.postech.adjt.cart.controller;

import br.com.fiap.postech.adjt.cart.client.ProductClient;
import br.com.fiap.postech.adjt.cart.dto.*;
import br.com.fiap.postech.adjt.cart.repository.ItemRepository;
import br.com.fiap.postech.adjt.cart.service.impl.CartServiceImpl;
import br.com.fiap.postech.adjt.cart.service.impl.ProductServiceImpl;
import br.com.fiap.postech.adjt.cart.test.utils.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartServiceImpl cartService;

    @MockBean
    private ProductServiceImpl productService;

    @MockBean
    private ProductClient productClient;

    @MockBean
    private ItemRepository itemRepository;

    private static final String URL_BASE = "http://localhost:8081/cart";

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        ReflectionTestUtils.setField(cartService, "productService", productService);
        ReflectionTestUtils.setField(cartService, "itemRepository", itemRepository);
        ReflectionTestUtils.setField(productService, "productClient", productClient);
    }


    @Nested
    class TestsAddItem {

        @Test
        void testAddItemSuccess() throws Exception {
            given(cartService.addItem(Mockito.any()))
                    .willReturn(ResponseEntity.ok(new CartResponseRecord(Messages.ADD_ITEM_SUCCESS)));
            MvcResult result = mockMvc.perform(
                    post(URL_BASE + "/items")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(TestUtils.buildCartRequest()))
            ).andReturn();
            CartResponseRecord resp = objectMapper.readValue(result.getResponse().getContentAsString(), CartResponseRecord.class);
            assertEquals(Messages.ADD_ITEM_SUCCESS, resp.message());
            assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        }

        @Test
        void testAddItemErrorWhenConsumerIdInvalid() throws Exception {
            CartRequest cartRequest = TestUtils.buildCartRequest();
            cartRequest.setConsumerId("consumer-id-invalid");
            MvcResult result = mockMvc.perform(
                    post(URL_BASE + "/items")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(cartRequest))
            ).andReturn();
            ErrorRecord error = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorRecord.class);
            assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
            assertEquals("Invalid consumerId format", error.error());
        }

        @Test
        void testAddItemErrorWhenItemIdNotExist() throws Exception {
            given(cartService.addItem(Mockito.any())).willCallRealMethod();
            given(productService.findProductById(Mockito.anyLong())).willCallRealMethod();
            given(productClient.findProductById(Mockito.anyLong())).willThrow(FeignException.NotFound.class);
            MvcResult result = mockMvc.perform(
                    post(URL_BASE + "/items")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(TestUtils.buildCartRequest()))
            ).andReturn();
            ErrorRecord error = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorRecord.class);
            assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
            assertEquals("Invalid itemId does not exist", error.error());
        }

        @Test
        void testAddItemErrorWhenInvalidItemQuantity() throws Exception {
            CartRequest cartRequest = TestUtils.buildCartRequest();
            cartRequest.setQuantity(0);
            MvcResult result = mockMvc.perform(
                    post(URL_BASE + "/items")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(cartRequest))
            ).andReturn();
            ErrorRecord error = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorRecord.class);
            assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
            assertEquals("Invalid itemId quantity", error.error());
        }

    }


    @Nested
    class TestsDecrementItem {

        @Test
        void testAddItemSuccess() throws Exception {
            given(cartService.decrementItem(Mockito.any()))
                    .willReturn(ResponseEntity.ok(new CartResponseRecord(Messages.DEC_ITEM_SUCCESS)));
            MvcResult result = mockMvc.perform(
                    delete(URL_BASE + "/item")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(TestUtils.buildCartRequest()))
            ).andReturn();
            CartResponseRecord resp = objectMapper.readValue(result.getResponse().getContentAsString(), CartResponseRecord.class);
            assertEquals(Messages.DEC_ITEM_SUCCESS, resp.message());
            assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        }

        @Test
        void testAddItemErrorWhenConsumerIdInvalid() throws Exception {
            CartRequest cartRequest = TestUtils.buildCartRequest();
            cartRequest.setConsumerId("consumer-id-invalid");
            MvcResult result = mockMvc.perform(
                    delete(URL_BASE + "/item")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(cartRequest))
            ).andReturn();
            ErrorRecord error = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorRecord.class);
            assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
            assertEquals("Invalid consumerId format", error.error());
        }

    }


    @Nested
    class TestsIncrementItem {

        @Test
        void testAddItemSuccess() throws Exception {
            given(cartService.incrementItem(Mockito.any()))
                    .willReturn(ResponseEntity.ok(new CartResponseRecord(Messages.INC_ITEM_SUCCESS)));
            MvcResult result = mockMvc.perform(
                    put(URL_BASE + "/item")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(TestUtils.buildCartRequest()))
            ).andReturn();
            CartResponseRecord resp = objectMapper.readValue(result.getResponse().getContentAsString(), CartResponseRecord.class);
            assertEquals(Messages.INC_ITEM_SUCCESS, resp.message());
            assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        }

        @Test
        void testAddItemErrorWhenConsumerIdInvalid() throws Exception {
            CartRequest cartRequest = TestUtils.buildCartRequest();
            cartRequest.setConsumerId("consumer-id-invalid");
            MvcResult result = mockMvc.perform(
                    put(URL_BASE + "/item")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(cartRequest))
            ).andReturn();
            ErrorRecord error = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorRecord.class);
            assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
            assertEquals("Invalid consumerId format", error.error());
        }

        @Test
        void testAddItemErrorWhenInvalidItem() throws Exception {
            CartRequest cartRequest = TestUtils.buildCartRequest();
            given(cartService.incrementItem(Mockito.any())).willCallRealMethod();
            given(itemRepository.findById(Mockito.any())).willReturn(Optional.empty());
            MvcResult result = mockMvc.perform(
                    put(URL_BASE + "/item")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(cartRequest))
            ).andReturn();
            ErrorRecord error = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorRecord.class);
            assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
            assertEquals("Invalid itemId", error.error());
        }

    }


    @Nested
    class TestsFindCartByConsumerId {

        @Test
        void testFindCartByConsumerIdSuccess() throws Exception {
            given(cartService.findCartByConsumerId(Mockito.any()))
                    .willReturn(ResponseEntity.ok(TestUtils.buildCartDto()));
            MvcResult result = mockMvc.perform(
                    get(URL_BASE)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(TestUtils.buildCartRequest()))
            ).andReturn();
            CartDto resp = objectMapper.readValue(result.getResponse().getContentAsString(), CartDto.class);
            assertEquals(1, resp.items().size());
            assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        }

        @Test
        void testAddItemErrorWhenCartIsEmpty() throws Exception {
            given(cartService.findCartByConsumerId(Mockito.any())).willCallRealMethod();
            given(itemRepository.findAllByConsumerId(Mockito.any(UUID.class))).willReturn(List.of());
            MvcResult result = mockMvc.perform(
                    get(URL_BASE)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(TestUtils.buildCartRequest()))
            ).andReturn();
            ErrorRecord error = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorRecord.class);
            assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
            assertEquals("Empty cart", error.error());
        }

        @Test
        void testAddItemErrorWhenConsumerIdInvalid() throws Exception {
            CartRequest cartRequest = TestUtils.buildCartRequest();
            cartRequest.setConsumerId("consumer-id-invalid");
            MvcResult result = mockMvc.perform(
                    get(URL_BASE)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(cartRequest))
            ).andReturn();
            ErrorRecord error = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorRecord.class);
            assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
            assertEquals("Invalid consumerId format", error.error());
        }

    }


    @Nested
    class TestsDeleteCartByConsumerId {

        @Test
        void testFindCartByConsumerIdSuccess() throws Exception {
            given(cartService.deleteCartByConsumerId(Mockito.any()))
                    .willReturn(ResponseEntity.ok(new CartResponseRecord(Messages.CART_CLEAR_ITEMS)));
            MvcResult result = mockMvc.perform(
                    delete(URL_BASE)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(TestUtils.buildCartRequest()))
            ).andReturn();
            CartResponseRecord resp = objectMapper.readValue(result.getResponse().getContentAsString(), CartResponseRecord.class);
            assertEquals("Items removed from cart successfully", resp.message());
            assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        }

        @Test
        void testAddItemErrorWhenConsumerIdInvalid() throws Exception {
            CartRequest cartRequest = TestUtils.buildCartRequest();
            cartRequest.setConsumerId("consumer-id-invalid");
            MvcResult result = mockMvc.perform(
                    delete(URL_BASE)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(cartRequest))
            ).andReturn();
            ErrorRecord error = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorRecord.class);
            assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
            assertEquals("Invalid consumerId format", error.error());
        }

    }

}
