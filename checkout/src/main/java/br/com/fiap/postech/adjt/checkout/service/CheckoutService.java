package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.controller.request.CreateOrderRequest;
import br.com.fiap.postech.adjt.checkout.dto.*;
import br.com.fiap.postech.adjt.checkout.entity.Item;
import br.com.fiap.postech.adjt.checkout.entity.Order;
import br.com.fiap.postech.adjt.checkout.entity.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class CheckoutService {

    @Value("${payment-api-key}")
    private String paymentApiKey;

    @Value("${cart-uri}")
    private String cartURI;

    @Value("${payment-api-uri}")
    private String paymentApiUri;

    private final OrderRepository orderRepository;

    private final RestClient restClient = RestClient.create();

    public CheckoutService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(CreateOrderRequest createOrderRequest) {

        CartDTO cart = findCart(UUID.fromString(createOrderRequest.consumerId()));

        Order order = new Order();
        order.setOrderId(UUID.randomUUID());
        order.setStatus(PaymentStatus.pending);
        order.setPaymentType(createOrderRequest.payment_method().type());
        order.setValue(createOrderRequest.amount());
        List<Item> items = new ArrayList<>();
        cart.items().forEach(itemDTO -> {
            Item item = new Item();
            item.setId(UUID.randomUUID());
            item.setItemId(itemDTO.itemId());
            item.setQnt(itemDTO.qnt());
            item.setOrder(order);
            items.add(item);
        });
        order.setItems(items);
        orderRepository.save(order);

        ProcessPaymentDTO processPaymentDTO = new ProcessPaymentDTO(
                order.getOrderId().toString(),
                order.getValue(),
                "BRL",
                createOrderRequest.payment_method()
        );
        PaymentDTO paymentDTO = processPayment(processPaymentDTO);

        if (!paymentDTO.status().equals(order.getStatus())) {
            order.setStatus(paymentDTO.status());
            orderRepository.save(order);
        }

        removeAllItems(UUID.fromString(createOrderRequest.consumerId()));

        return order;
    }

    public List<Order> findOrdersByConsumerId(UUID consumerId) {
        return orderRepository.findByConsumerId(consumerId);
    }

    public Order findOrderById(UUID orderId) {
        return orderRepository.findByOrderId(orderId);
    }

    CartDTO findCart(UUID consumerId) {
         FindCartDTO findCartRequest = new FindCartDTO(consumerId.toString());
         return restClient
                 .method(HttpMethod.GET)
                 .uri(cartURI)
                 .contentType(APPLICATION_JSON)
                 .body(findCartRequest)
                 .retrieve()
                 .toEntity(CartDTO.class).getBody();
    }

    void removeAllItems(UUID consumerId) {
        RemoveAllItemsDTO removeAllItemsRequest = new RemoveAllItemsDTO(consumerId.toString());
         restClient
                 .method(HttpMethod.DELETE)
                 .uri(cartURI)
                 .contentType(APPLICATION_JSON)
                 .body(removeAllItemsRequest)
                 .retrieve()
                 .toEntity(CartDTO.class).getBody();
    }

    PaymentDTO processPayment(ProcessPaymentDTO processPaymentDTO) {
         return restClient
                 .post()
                 .uri(paymentApiUri)
                 .contentType(APPLICATION_JSON)
                 .header("apiKey", paymentApiKey)
                 .body(processPaymentDTO)
                 .retrieve()
                 .toEntity(PaymentDTO.class).getBody();
    }

}
