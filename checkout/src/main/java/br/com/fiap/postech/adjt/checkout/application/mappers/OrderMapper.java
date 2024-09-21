package br.com.fiap.postech.adjt.checkout.application.mappers;

import br.com.fiap.postech.adjt.checkout.application.dto.OrderDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.OrderItemDTO;
import br.com.fiap.postech.adjt.checkout.domain.entity.Order;
import br.com.fiap.postech.adjt.checkout.domain.entity.OrderItem;
import org.springframework.stereotype.Service;

@Service
public class OrderMapper {

    public OrderDTO toOrderDTO(Order order) {
        return new OrderDTO(
                order.getOrderId().toString(),
                order.getPaymentType(),
                order.getValue(),
                order.getPaymentStatus(),
                order.getItems().stream().map(this::toOrderItemDTO).toList());
    }

    private OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        return new OrderItemDTO(orderItem.getCodItem(), orderItem.getQuantity());
    }
}
