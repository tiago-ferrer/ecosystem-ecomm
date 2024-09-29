package br.com.fiap.postech.adjt.checkout.domain.gateway;

import br.com.fiap.postech.adjt.checkout.dataprovider.database.entity.OrderEntity;
import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderGateway {
    OrderModel createNewOrder(OrderModel orderModel);

    List<OrderModel> findByConsumerId(String consumerId);

    Optional<OrderModel> findById(UUID uuid);
}
