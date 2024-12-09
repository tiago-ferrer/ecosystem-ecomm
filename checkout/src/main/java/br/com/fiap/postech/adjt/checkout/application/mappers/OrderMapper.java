package br.com.fiap.postech.adjt.checkout.application.mappers;

import br.com.fiap.postech.adjt.checkout.application.dto.OrderDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.OrderItemDTO;
import br.com.fiap.postech.adjt.checkout.dataprovider.database.entity.OrderEntity;
import br.com.fiap.postech.adjt.checkout.dataprovider.database.entity.OrderItemEntity;
import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderItemModel;
import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderModel;
import org.springframework.stereotype.Service;

@Service
public class OrderMapper {

    public OrderDTO toOrderDTO(OrderModel order) {
        return new OrderDTO(
                order.getOrderId().toString(),
                order.getPaymentType(),
                order.getValue(),
                order.getPaymentStatus(),
                order.getItems().stream().map(this::toOrderItemDTO).toList());
    }

    private OrderItemDTO toOrderItemDTO(OrderItemModel orderItem) {
        return new OrderItemDTO(orderItem.getCodItem(), orderItem.getQuantity());
    }
}
