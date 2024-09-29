package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.dto.CartDTO;
import br.com.fiap.postech.adjt.checkout.dto.CheckoutRequestDTO;
import br.com.fiap.postech.adjt.checkout.dto.OrderItemDTO;
import br.com.fiap.postech.adjt.checkout.model.Order;
import br.com.fiap.postech.adjt.checkout.model.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order createOrderFromRequest(CheckoutRequestDTO request) {
        CartDTO cart = cartService.getCart(request.consumerId());

        List<OrderItemDTO> orderItems = cart.items().stream()
                .map(item -> new OrderItemDTO(item.consumerId(), item.itemId(), item.quantity()))
                .toList();

        UUID newOrderId = UUID.randomUUID();

        Order order = new Order(
                newOrderId,
                request.consumerId(),
                orderItems,
                request.paymentMethod().type(),
                request.amount(),
                PaymentStatus.PENDING
        );

        return orderRepository.save(order);
    }

    public List<Order> getOrdersByConsumerId(UUID consumerId) {
        return orderRepository.findByConsumerId(consumerId);
    }

    public Order getOrderById(UUID orderId) {
        return orderRepository.findByOrderId(orderId);
    }

}
