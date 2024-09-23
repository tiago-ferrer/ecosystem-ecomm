package br.com.fiap.postech.adjt.checkout.dataprovider.database.gateway;

import br.com.fiap.postech.adjt.checkout.dataprovider.database.entity.OrderEntity;
import br.com.fiap.postech.adjt.checkout.dataprovider.database.mapper.OrderMapper;
import br.com.fiap.postech.adjt.checkout.dataprovider.database.repository.OrderRepository;
import br.com.fiap.postech.adjt.checkout.domain.gateway.OrderGateway;
import br.com.fiap.postech.adjt.checkout.domain.model.order.OrderModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderGatewayImpl implements OrderGateway {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderModel createNewOrder(OrderModel orderModel) {
        OrderEntity orderEntity = orderMapper.toOrderEntity(orderModel);
        orderEntity = orderRepository.save(orderEntity);
        return orderMapper.toOrderModel(orderEntity);
    }

}
