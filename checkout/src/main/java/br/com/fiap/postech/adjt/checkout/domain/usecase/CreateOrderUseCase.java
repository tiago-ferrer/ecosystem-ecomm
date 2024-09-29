package br.com.fiap.postech.adjt.checkout.domain.usecase;

import br.com.fiap.postech.adjt.checkout.domain.exception.AppException;
import br.com.fiap.postech.adjt.checkout.domain.exception.constants.ErrorConstants;
import br.com.fiap.postech.adjt.checkout.domain.gateway.CartGateway;
import br.com.fiap.postech.adjt.checkout.domain.gateway.OrderGateway;
import br.com.fiap.postech.adjt.checkout.domain.gateway.PaymentGateway;
import br.com.fiap.postech.adjt.checkout.domain.model.cart.CartModel;
import br.com.fiap.postech.adjt.checkout.domain.model.enums.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderItemModel;
import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderModel;
import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderStatusModel;
import br.com.fiap.postech.adjt.checkout.domain.model.payment.CheckoutModel;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CreateOrderUseCase {
    private final OrderGateway orderGateway;
    private final CartGateway cartGateway;
    private final PaymentGateway paymentGateway;

    public CreateOrderUseCase(OrderGateway orderGateway, CartGateway cartGateway, PaymentGateway paymentGateway) {
        this.orderGateway = orderGateway;
        this.cartGateway = cartGateway;
        this.paymentGateway = paymentGateway;
    }

    public OrderModel createOrder(CheckoutModel checkoutModel) throws AppException {
        CartModel cartModel;
        try {
            cartModel = cartGateway.getCartByConsumerId(checkoutModel.getConsumerId());
        } catch(Exception e) {
            throw new AppException(ErrorConstants.USER_ID_FORMAT_INVALID);
        }

        OrderModel orderModel = OrderModel.builder()
                .consumerId(checkoutModel.getConsumerId())
                .value(checkoutModel.getAmount())
                .items(cartModel.getItems().stream().map(
                        cartItem -> OrderItemModel.builder()
                                .quantity(cartItem.getQuantity())
                                .codItem(cartItem.getItemId())
                                .build()).toList())
                .paymentType(checkoutModel.getPaymentMethod().getType())
                .paymentStatus(PaymentStatus.pending)
                .build();

        orderModel = orderGateway.createNewOrder(orderModel);
        cartGateway.emptyCartByConsumerId(checkoutModel.getConsumerId());
        paymentGateway.processPayment(checkoutModel, orderModel.getOrderId());
        return orderModel;
    }

    public List<OrderModel> getOrderByConsumerId(String consumerId) throws AppException {

        if (Objects.isNull(consumerId) || consumerId.isEmpty() || !isValidUUID(consumerId)) {
            throw new AppException(ErrorConstants.USER_ID_FORMAT_INVALID);
        }

        return orderGateway.findByConsumerId(consumerId);
    }

    public OrderModel getOrderById(String orderId) throws AppException {

        if (Objects.isNull(orderId) || orderId.isEmpty() || !isValidUUID(orderId)) {
            throw new AppException(ErrorConstants.ORDER_ID_FORMAT_INVALID);
        }
        Optional<OrderModel> optionalOrder = orderGateway.findOrderModelById(UUID.fromString(orderId));
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
