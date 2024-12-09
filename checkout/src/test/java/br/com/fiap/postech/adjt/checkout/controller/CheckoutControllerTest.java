package br.com.fiap.postech.adjt.checkout.controller;

import br.com.fiap.postech.adjt.checkout.controller.request.CreateOrderRequest;
import br.com.fiap.postech.adjt.checkout.controller.request.PaymentMethodFieldsRequest;
import br.com.fiap.postech.adjt.checkout.controller.request.PaymentMethodRequest;
import br.com.fiap.postech.adjt.checkout.entity.Item;
import br.com.fiap.postech.adjt.checkout.entity.Order;
import br.com.fiap.postech.adjt.checkout.entity.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.service.CheckoutService;
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
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CheckoutControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CheckoutService service;

    AutoCloseable mock;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
        CheckoutController controller = new CheckoutController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Nested
    class CreateOrder {

        @Test
        void shouldCreateOrder() throws Exception {
            Order order = new Order();
            order.setOrderId(UUID.randomUUID());
            order.setStatus(PaymentStatus.pending);
            when(service.createOrder(any(CreateOrderRequest.class)))
                    .thenReturn(order);
            CreateOrderRequest createOrderRequest = buildCreateOrderRequest();
            mockMvc.perform(
                    post("/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(createOrderRequest)))
                    .andExpect(status().is2xxSuccessful());
        }

        @Test
        void shouldInvalidConsumerId() throws Exception {
            CreateOrderRequest createOrderRequest = buildCreateOrderRequestWithInvalidConsumerId();
            mockMvc.perform(
                            post("/")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(createOrderRequest)))
                    .andExpect(status().is4xxClientError());
        }

        @Test
        void shouldThrowException() throws Exception {
            CreateOrderRequest createOrderRequest = buildCreateOrderRequest();
            when(service.createOrder(any(CreateOrderRequest.class)))
                    .thenThrow(HttpServerErrorException.InternalServerError.class);
            mockMvc.perform(
                            post("/")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(asJsonString(createOrderRequest)))
                    .andExpect(status().is5xxServerError());
        }

    }

    @Nested
    class FindOrdersByConsumerId {

        @Test
        void shouldFindOrdersByConsumerId() throws Exception {
            UUID consumerId = UUID.randomUUID();
            Order order = buildOrder();
            List<Order> orders = new ArrayList<>();

            Item item = new Item();
            item.setId(UUID.randomUUID());
            item.setItemId(1L);
            item.setQnt(2L);

            List<Item> items = new ArrayList<>();
            items.add(item);

            order.setItems(items);

            orders.add(order);
            when(service.findOrdersByConsumerId(any(UUID.class)))
                    .thenReturn(orders);
            mockMvc.perform(get("/{consumerId}", consumerId))
                    .andExpect(status().is2xxSuccessful());
        }

        @Test
        void shouldInvalidConsumerId() throws Exception {
            mockMvc.perform(get("/{consumerId}", "1234"))
                    .andExpect(status().is4xxClientError());
        }

        @Test
        void shouldThrowException() throws Exception {
            when(service.findOrdersByConsumerId(any(UUID.class)))
                    .thenThrow(HttpServerErrorException.InternalServerError.class);
            mockMvc.perform(get("/{consumerId}", UUID.randomUUID().toString()))
                    .andExpect(status().is5xxServerError());
        }

    }

    @Nested
    class FindOrderById {

        @Test
        void shouldFindOrderById() throws Exception {
            UUID orderId = UUID.randomUUID();
            Order order = buildOrder();
            Item item = new Item();
            item.setId(UUID.randomUUID());
            item.setItemId(1L);
            item.setQnt(2L);

            List<Item> items = new ArrayList<>();
            items.add(item);

            order.setItems(items);
            when(service.findOrderById(any(UUID.class)))
                    .thenReturn(order);
            mockMvc.perform(get("/by-order-id/{orderId}", orderId))
                    .andExpect(status().is2xxSuccessful());
        }

        @Test
        void shouldInvalidConsumerId() throws Exception {
            mockMvc.perform(get("/by-order-id/{orderId}", "1234"))
                    .andExpect(status().is4xxClientError());
        }

        @Test
        void shouldThrowException() throws Exception {
            when(service.findOrderById(any(UUID.class)))
                    .thenThrow(HttpServerErrorException.InternalServerError.class);
            mockMvc.perform(get("/by-order-id/{orderId}", UUID.randomUUID()))
                    .andExpect(status().is5xxServerError());
        }

    }

    private CreateOrderRequest buildCreateOrderRequest() {
        PaymentMethodFieldsRequest paymentMethodFieldsRequest = new PaymentMethodFieldsRequest(
            "1234",
                "1",
                "2024",
                "123",
                "Lucas"
        );
        PaymentMethodRequest paymentMethodRequest = new PaymentMethodRequest(
                "test",
                paymentMethodFieldsRequest
        );
        return new CreateOrderRequest(
                UUID.randomUUID().toString(),
                10000L,
                "BRL",
                paymentMethodRequest
        );
    }

    private CreateOrderRequest buildCreateOrderRequestWithInvalidConsumerId() {
        PaymentMethodFieldsRequest paymentMethodFieldsRequest = new PaymentMethodFieldsRequest(
                "1234",
                "1",
                "2024",
                "123",
                "Lucas"
        );
        PaymentMethodRequest paymentMethodRequest = new PaymentMethodRequest(
                "test",
                paymentMethodFieldsRequest
        );
        return new CreateOrderRequest(
                "1234",
                10000L,
                "BRL",
                paymentMethodRequest
        );
    }

    private Order buildOrder() {
        Order order = new Order();
        order.setConsumerId(UUID.randomUUID());
        order.setOrderId(UUID.randomUUID());
        order.setValue(1000L);
        order.setStatus(PaymentStatus.pending);
        List<Item> items = new ArrayList<>();
        order.setItems(items);
        return order;
    }

    private String asJsonString(final Object object) throws JsonProcessingException {
        return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(object);
    }

}
