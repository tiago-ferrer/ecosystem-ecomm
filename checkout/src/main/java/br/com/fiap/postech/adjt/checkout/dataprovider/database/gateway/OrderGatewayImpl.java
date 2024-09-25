package br.com.fiap.postech.adjt.checkout.dataprovider.database.gateway;

import br.com.fiap.postech.adjt.checkout.dataprovider.database.entity.OrderEntity;
import br.com.fiap.postech.adjt.checkout.dataprovider.database.entity.OrderItemEntity;
import br.com.fiap.postech.adjt.checkout.dataprovider.database.mapper.OrderMapper;
import br.com.fiap.postech.adjt.checkout.dataprovider.database.repository.OrderRepository;
import br.com.fiap.postech.adjt.checkout.domain.gateway.OrderGateway;
import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderGatewayImpl implements OrderGateway {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final br.com.fiap.postech.adjt.checkout.application.mappers.OrderMapper orderMapperUnder;

    @Override
    public OrderModel createNewOrder(OrderModel orderModel) {
        OrderEntity orderEntity = orderMapper.toOrderEntity(orderModel);

        List<OrderItemEntity> list = orderEntity.getItems();
        for (OrderItemEntity orderItemEntity : list) {
            orderItemEntity.setOrder(orderEntity);
        }
        orderEntity.setItems(list);
        orderEntity = orderRepository.save(orderEntity);
        return orderMapper.toOrderModel(orderEntity);
    }

    @Override
    public List<OrderModel> findByConsumerId(String consumerId) {
        return orderRepository.findByConsumerId(consumerId).stream().map(orderMapperUnder::toOrderModel).toList();
    }

    @Override
    public Optional<OrderModel> findOrderModelById(UUID uuid) {
        OrderEntity teste = orderRepository.findById(uuid).orElse(null);

        return orderRepository.findByOrderId(uuid).map(orderMapper::toOrderModel);
    }

}
