package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.controller.request.CreateOrderRequest;
import br.com.fiap.postech.adjt.checkout.dto.*;
import br.com.fiap.postech.adjt.checkout.entity.Item;
import br.com.fiap.postech.adjt.checkout.entity.Order;
import br.com.fiap.postech.adjt.checkout.entity.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.repository.OrderRepository;
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

    public Order createOrder(CreateOrderRequest createOrderRequest) {
        // 1.obter carrinho
        CartDTO cart = findCart(UUID.fromString(createOrderRequest.consumerId()));

        // 2. persistir pedido
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

        // 3. efetuar pagamento
        ProcessPaymentDTO processPaymentDTO = new ProcessPaymentDTO(
                order.getOrderId().toString(),
                order.getValue().longValue(),
                "BRL",
                createOrderRequest.payment_method()
        );
        PaymentDTO paymentDTO = processPayment(processPaymentDTO);

        // 4. tempo de resposta menor que 300ms

        // 5. processamento assíncrono

        // 6. atualizar status do pedido dependendo da resposta da api de pagamento
        if (!paymentDTO.status().equals(order.getStatus())) {
            order.setStatus(paymentDTO.status());
            orderRepository.save(order);
        }

        // 7. limpar carrinho
        removeAllItems(UUID.fromString(createOrderRequest.consumerId()));

        return order;
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

     private PaymentDTO processPayment(ProcessPaymentDTO processPaymentDTO) {
         return restClient
                 .post()
                 .uri("https://payment-api-latest.onrender.com/create-payment")
                 .contentType(APPLICATION_JSON)
                 .header("apiKey", "8aa4f012c4c6b43e6dfb427596381b0640618323c196f70022e02daadf639ac0")
                 .body(processPaymentDTO)
                 .retrieve()
                 .toEntity(PaymentDTO.class).getBody();
     }

}
