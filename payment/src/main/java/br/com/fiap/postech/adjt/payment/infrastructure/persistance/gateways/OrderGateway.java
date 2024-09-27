package br.com.fiap.postech.adjt.payment.infrastructure.persistance.gateways;

import br.com.fiap.postech.adjt.payment.domain.entities.Item;
import br.com.fiap.postech.adjt.payment.domain.entities.enums.OrderStatus;
import br.com.fiap.postech.adjt.payment.infrastructure.dtos.Fields;
import br.com.fiap.postech.adjt.payment.infrastructure.dtos.PaymentMethod;
import br.com.fiap.postech.adjt.payment.infrastructure.dtos.PaymentRequest;
import br.com.fiap.postech.adjt.payment.infrastructure.persistance.entities.ItemEntity;
import br.com.fiap.postech.adjt.payment.infrastructure.persistance.entities.OrderEntity;
import br.com.fiap.postech.adjt.payment.infrastructure.persistance.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderGateway {
    private final OrderRepository orderRepository;

    public void updateOrderStatus(UUID orderId, OrderStatus status) throws BadRequestException {

        Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
        if (orderEntity.isEmpty()) {
            throw new BadRequestException("A ordem escolhida não existe");
        }
        OrderEntity order = orderEntity.get();
        order.setPaymentStatus(status);
        orderRepository.save(order);
    }

    public void updateOrderList(UUID orderId, List<Item> items) throws BadRequestException {
        Optional<OrderEntity> orderEntity = orderRepository.findById(orderId);
        if (orderEntity.isEmpty()) {
            throw new BadRequestException("A ordem escolhida não existe");
        }

        OrderEntity order = orderEntity.get();
        List<ItemEntity> itemsEntities = items.stream()
                .map(item -> new ItemEntity(item.itemId().toString(), item.qnt()))
                .toList();
        order.setItems(itemsEntities);
        orderRepository.save(order);
    }

    public  PaymentRequest findById(UUID orderId) throws BadRequestException {
        Optional<OrderEntity> order = orderRepository.findById(orderId);
        if (order.isEmpty()) throw new BadRequestException();
        OrderEntity orderEntity = order.get();
        return new PaymentRequest(
                orderId,
                orderEntity.getValue(),
                orderEntity.getCurrency(),
                new PaymentMethod(orderEntity.getPaymentType(), new Fields(
                        orderEntity.getFieldsNumber(),
                        orderEntity.getFieldsExpirationMonth(),
                        orderEntity.getFieldsExpirationYear(),
                        orderEntity.getCvv(),
                        orderEntity.getFieldsName()
                )
        ));
    }
}
