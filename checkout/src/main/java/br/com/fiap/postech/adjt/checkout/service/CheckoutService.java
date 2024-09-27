package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.dto.*;
import br.com.fiap.postech.adjt.checkout.model.Order;
import br.com.fiap.postech.adjt.checkout.model.OrderItem;
import br.com.fiap.postech.adjt.checkout.model.OrderStatus;
import br.com.fiap.postech.adjt.checkout.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CheckoutService {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    public CheckoutService(CartService cartService, PaymentService paymentService, OrderService orderService, OrderRepository orderRepository) {
        this.cartService = cartService;
        this.paymentService = paymentService;
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    public CheckoutResponseDTO processCheckout(CheckoutRequestDTO checkoutRequest) {
        System.out.println("Processando checkout para o consumerId: " + checkoutRequest.consumerId());

        if (checkoutRequest.paymentMethod() == null) {
            throw new IllegalArgumentException("Método de pagamento não pode ser nulo");
        }

        CartDTO cart = cartService.getCart(checkoutRequest.consumerId());
        if (cart == null || cart.items().isEmpty()) {
            throw new IllegalArgumentException("Carrinho de compras vazio");
        }

        Order order = new Order();
        order.setConsumerId(checkoutRequest.consumerId());
        order.setStatus(OrderStatus.PENDING);

        order = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItemDTO cartItem : cart.items()) {
            Long itemId = cartItem.itemId();
            orderItems.add(new OrderItem(itemId, cartItem.quantity(), order));
        }

        order.setItems(orderItems);

        orderRepository.save(order);

        return new CheckoutResponseDTO(order.getId().toString(), order.getStatus().name());
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
