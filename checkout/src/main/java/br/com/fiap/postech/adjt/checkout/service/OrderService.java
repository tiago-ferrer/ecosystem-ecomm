package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.model.Cart;
import br.com.fiap.postech.adjt.checkout.model.Checkout;
import br.com.fiap.postech.adjt.checkout.model.Order;
import br.com.fiap.postech.adjt.checkout.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createAndSaveOrder(Cart cart, Checkout checkout) {
        return createOrderFromCart(cart, checkout);
    }


    public Order getOrderByOrderId(String orderId) {
        UUID orderUuid = UUID.fromString(orderId);
        return orderRepository.findByOrderId(orderUuid);
    }

    public List<Order> getOrderByConsumerId(String consumerId) {
        UUID orderUuid = UUID.fromString(consumerId);
       return orderRepository.findByConsumerId(orderUuid);
    }

    @Transactional
    public void updateOrder(Order order) {
        Order existingOrder = orderRepository.findById(order.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        existingOrder.setPaymentStatus(order.getPaymentStatus());
        orderRepository.save(existingOrder);
    }

    @Transactional
    protected Order createOrderFromCart(Cart cart, Checkout checkout) {
        Order order = new Order();
        order.setConsumerId(checkout.getConsumerId());
        order.setItems(cart.getItems());
        order.setCurrency(checkout.getCurrency());
        order.setValue(checkout.getAmount());
        order.setPaymentMethodType(checkout.getPaymentMethod().getType());
        order.setPaymentStatus(checkout.getStatus());
        orderRepository.save(order);
        return order;
    }
}
