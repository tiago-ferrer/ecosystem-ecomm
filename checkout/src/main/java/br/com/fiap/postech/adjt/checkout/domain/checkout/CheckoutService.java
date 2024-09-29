package br.com.fiap.postech.adjt.checkout.domain.checkout;

import br.com.fiap.postech.adjt.checkout.controller.order.CreateOrderRequest;
import br.com.fiap.postech.adjt.checkout.domain.checkout.dto.*;
import br.com.fiap.postech.adjt.checkout.domain.order.Item;
import br.com.fiap.postech.adjt.checkout.domain.order.Order;
import br.com.fiap.postech.adjt.checkout.domain.order.OrderRepository;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class CheckoutService {

    private final OrderRepository orderRepository;
    private final RestClient restClient = RestClient.create();

    public CheckoutService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    public List<Order> findOrdersByConsumerId(UUID consumerId) {
        return orderRepository.findByConsumerId(consumerId);
    }

    public Order findOrderById(UUID orderId) {
        return orderRepository.findByOrderId(orderId);
    }

     private CartDTO findCart(UUID consumerId) {
//         String cartURI = "http://localhost:8081";
        String cartURI = "http://cart:8081";
         FindCartDTO findCartRequest = new FindCartDTO(consumerId.toString());
         return restClient
                 .method(HttpMethod.GET)
                 .uri(cartURI)
                 .contentType(APPLICATION_JSON)
                 .body(findCartRequest)
                 .retrieve()
                 .toEntity(CartDTO.class).getBody();
     }

    private PaymentDTO processPayment(ProcessPaymentDTO processPaymentDTO) {
        return restClient
                .post()
                .uri("https://payment-api-latest.onrender.com/create-payment")
                .contentType(APPLICATION_JSON)
                .header("apiKey", "dcc544ea2508aff06fb9079612aadad903c0aa68059a62c8a41c01f81333b62a")
                .body(processPaymentDTO)
                .retrieve()
                .toEntity(PaymentDTO.class).getBody();
    }
     private void removeAllItems(UUID consumerId) {
//         String cartURI = "http://localhost:8081";
        String cartURI = "http://cart:8081";
        RemoveAllItemsDTO removeAllItemsRequest = new RemoveAllItemsDTO(consumerId.toString());
         restClient
                 .method(HttpMethod.DELETE)
                 .uri(cartURI)
                 .contentType(APPLICATION_JSON)
                 .body(removeAllItemsRequest)
                 .retrieve()
                 .toEntity(CartDTO.class).getBody();
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
                order.getValue().longValue(),
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
}
