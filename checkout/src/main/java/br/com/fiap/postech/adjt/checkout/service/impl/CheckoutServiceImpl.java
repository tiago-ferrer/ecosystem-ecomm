package br.com.fiap.postech.adjt.checkout.service.impl;

import br.com.fiap.postech.adjt.checkout.clients.CartClient;
import br.com.fiap.postech.adjt.checkout.controller.exception.NotFoundException;
import br.com.fiap.postech.adjt.checkout.model.Card;
import br.com.fiap.postech.adjt.checkout.model.CartItem;
import br.com.fiap.postech.adjt.checkout.model.Order;
import br.com.fiap.postech.adjt.checkout.model.dto.request.ClearCartRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.request.FindCartByCustomerIdRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.request.PaymentMethodRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.request.PaymentRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.response.CartItemResponse;
import br.com.fiap.postech.adjt.checkout.model.dto.response.CartResponse;
import br.com.fiap.postech.adjt.checkout.model.dto.response.CheckoutResponse;
import br.com.fiap.postech.adjt.checkout.model.dto.response.OrderCheckoutsResponse;
import br.com.fiap.postech.adjt.checkout.repository.OrderRepository;
import br.com.fiap.postech.adjt.checkout.service.CheckoutService;
import br.com.fiap.postech.adjt.checkout.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    private final CartClient cartClient;

    @Transactional
    public CheckoutResponse processCheckout(UUID consumerId, int amount, String currency,
                                            PaymentMethodRequest paymentMethod) {

        Order order = createPendingOrder(consumerId, amount, paymentMethod);

        PaymentRequest paymentRequest = new PaymentRequest(order.getOrderId().toString(), amount, currency,
                paymentMethod);

        CompletableFuture.runAsync(() -> paymentService.process(order, paymentRequest));

        return new CheckoutResponse(order.getConsumerId().toString(), order.getPaymentStatus());
    }

    private Order createPendingOrder(UUID consumerId, int amount, PaymentMethodRequest paymentMethod) {
        Order order = new Order();
        order.setConsumerId(consumerId);
        order.setItems(fetchCartItems(consumerId));
        order.setPaymentType(paymentMethod.getType());
        order.setValue(amount);
        order.setPaymentStatus("pending");

        Card card = new Card();
        card.setConsumerId(consumerId);
        card.setNumber(paymentMethod.getFields().getNumber());
        card.setExpiration_month(paymentMethod.getFields().getExpiration_month());
        card.setExpiration_year(paymentMethod.getFields().getExpiration_year());
        card.setCvv(paymentMethod.getFields().getCvv());
        card.setName(paymentMethod.getFields().getName());

        order.setCard(card);
        return orderRepository.save(order);
    }

    private List<CartItem> fetchCartItems(UUID consumerId) {
        try {
            FindCartByCustomerIdRequest findCartByCustomerIdRequest = new FindCartByCustomerIdRequest(consumerId.toString());
            CartResponse cartResponse = cartClient.consult(findCartByCustomerIdRequest);

            if (cartResponse != null && cartResponse.items() != null) {
                return cartResponse.items().stream()
                        .map(item -> new CartItem(item.itemId(), item.qnt())).toList();
            } else {
                return List.of();
            }
        } catch (Exception e) {
            throw new NotFoundException("Cart items not found for consumerId: " + consumerId);
        }
    }

    @Override
    public List<OrderCheckoutsResponse> getOrdersByConsumerId(UUID consumerId) {
        List<Order> orderEntities2 = orderRepository.findByConsumerId(consumerId);
        return toResponseList(orderEntities2);
    }

    @Override
    public OrderCheckoutsResponse getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found for orderId: " + orderId));

        return toResponse(order);
    }

    private OrderCheckoutsResponse toResponse(Order order) {
        List<CartItemResponse> items = order.getItems().stream()
                .map(it -> new CartItemResponse(it.getItemId(), it.getQuantity()))
                .collect(Collectors.toList());

        return new OrderCheckoutsResponse(
                order.getOrderId(),
                items,
                order.getPaymentType(),
                order.getValue(),
                order.getPaymentStatus()
        );
    }

    private List<OrderCheckoutsResponse> toResponseList(List<Order> orderEntities) {
        return orderEntities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}