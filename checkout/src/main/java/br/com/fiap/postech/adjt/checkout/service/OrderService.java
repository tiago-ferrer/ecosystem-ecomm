package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.model.Cart;
import br.com.fiap.postech.adjt.checkout.model.Checkout;
import br.com.fiap.postech.adjt.checkout.model.Order;
import br.com.fiap.postech.adjt.checkout.model.PaymentStatus;
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
    public Order saveOrder(Cart cart, Checkout checkout) {
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

    protected Order createOrderFromCart(Cart cart, Checkout checkout) {
        Order order = new Order();
        order.setConsumerId(cart.getConsumerId());
        order.setItemList(cart.getItemList());
        double total = cart.getItemList().stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();
        order.setTotalValue(total);
//        order.setPaymentMethodType(checkout.getPaymentMethod().getType());
        order.setPaymentStatus(checkout.getStatus());
        orderRepository.save(order);
        return order;
    }
}
