package br.com.fiap.postech.adjt.checkout.domain.usecase;

import br.com.fiap.postech.adjt.checkout.dataprovider.database.entity.OrderEntity;
import br.com.fiap.postech.adjt.checkout.dataprovider.database.repository.OrderRepository;
import br.com.fiap.postech.adjt.checkout.domain.exception.AppException;
import br.com.fiap.postech.adjt.checkout.domain.exception.constants.ErrorConstants;
import br.com.fiap.postech.adjt.checkout.domain.gateway.CartGateway;
import br.com.fiap.postech.adjt.checkout.domain.gateway.OrderGateway;
import br.com.fiap.postech.adjt.checkout.domain.gateway.PaymentGateway;
import br.com.fiap.postech.adjt.checkout.domain.model.cart.CartModel;
import br.com.fiap.postech.adjt.checkout.domain.model.enums.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderItemModel;
import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderModel;
import br.com.fiap.postech.adjt.checkout.domain.model.payment.CheckoutModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;

    private final OrderGateway orderGateway;
    private final CartGateway cartGateway;
    private final PaymentGateway paymentGateway;

    public CreateOrderUseCase(OrderRepository orderRepository, OrderGateway orderGateway, CartGateway cartGateway,
                              PaymentGateway paymentGateway) {
        this.orderRepository = orderRepository;
        this.orderGateway = orderGateway;
        this.cartGateway = cartGateway;
        this.paymentGateway = paymentGateway;
    }

    public OrderModel createOrder(CheckoutModel checkoutModel) throws AppException {
        CartModel cartModel = cartGateway.getCartByConsumerId(checkoutModel.getConsumerId());
        OrderModel orderModel = OrderModel.builder()
                .consumerId(checkoutModel.getConsumerId())
                .value(checkoutModel.getAmount())
                .items(cartModel.getItems().stream().map(
                        cartItem -> OrderItemModel.builder()
                                .quantity(cartItem.getQuantity())
                                .codItem(cartItem.getItemId())
                                .build()).collect(Collectors.toList()))
                .paymentType(checkoutModel.getPaymentMethod().getType())
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        orderModel = orderGateway.createNewOrder(orderModel);
        paymentGateway.sendPayment(orderModel);
        return orderModel;
    }

    public List<OrderEntity> getOrderByConsumerId(String consumerId) throws AppException {

        if (Objects.isNull(consumerId) || consumerId.isEmpty() || isValidUUID(consumerId)) {
            throw new AppException(ErrorConstants.USER_ID_FORMAT_INVALID);
        }

        return orderRepository.findByConsumerId(consumerId);
    }

    public OrderEntity getOrderById(String orderId) throws AppException {

        if (Objects.isNull(orderId) || orderId.isEmpty() || isValidUUID(orderId)) {
            throw new AppException(ErrorConstants.ORDER_ID_FORMAT_INVALID);
        }
        Optional<OrderEntity> optionalOrder = orderRepository.findById(UUID.fromString(orderId));
        if (optionalOrder.isEmpty()) {
            throw new AppException(ErrorConstants.ORDER_ID_NOT_FOUND);
        }
        return optionalOrder.get();

    }

    private boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
