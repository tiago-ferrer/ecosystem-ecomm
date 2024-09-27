package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.dto.ItemDTO;
import br.com.fiap.postech.adjt.checkout.dto.OrderDTO;
import br.com.fiap.postech.adjt.checkout.model.Cart;
import br.com.fiap.postech.adjt.checkout.model.Checkout;
import br.com.fiap.postech.adjt.checkout.model.Order;
import br.com.fiap.postech.adjt.checkout.model.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public OrderDTO findByOrderId(String orderId) {
        Order order = getOrderByOrderId(orderId);
        return order.toDTO(order);
    }

    public List<OrderDTO> findPaymentByConsumer(String consumerId) {
        List<Order> orders = getOrderByConsumerId(consumerId);
        List<OrderDTO> ordersDto = orders.stream()
                .map(order -> new OrderDTO(
                        order.getOrderId(),
                        order.getConsumerId().toString(),
                        order.getItems().stream()
                                .map(item -> new ItemDTO(item.getItemId(), item.getQnt()))
                                .collect(Collectors.toList()),
                        order.getCurrency().toString(),
                        order.getPaymentMethodType(),
                        order.getValue(),
                        order.getPaymentStatus()
                ))
                .collect(Collectors.toList());

        return ordersDto;
    }

    public List<Order> findPendingOrders() {
        return orderRepository.findByPaymentStatus(PaymentStatus.pending);
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
