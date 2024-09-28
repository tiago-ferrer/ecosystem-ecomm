package br.com.fiap.postech.adjt.checkout.infrastructure.persistance.gateways;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.fiap.postech.adjt.checkout.domain.entities.Item;
import br.com.fiap.postech.adjt.checkout.domain.entities.enums.OrderStatus;
import br.com.fiap.postech.adjt.checkout.infrastructure.controllers.exceptions.BadRequestException;
import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.BeforeSave;
import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.CheckoutRequest;
import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.OrderInfo;
import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.OrderInfoList;
import br.com.fiap.postech.adjt.checkout.infrastructure.persistance.entities.ItemEntity;
import br.com.fiap.postech.adjt.checkout.infrastructure.persistance.entities.OrderEntity;
import br.com.fiap.postech.adjt.checkout.infrastructure.persistance.repositories.OrderRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderGateway {
        private final OrderRepository orderRepository;

        public BeforeSave checkout(CheckoutRequest checkoutRequest, List<Item> items) {
                List<ItemEntity> itemsEntities = items.stream()
                                .map(item -> ItemEntity.builder()
                                                .qnt(item.qnt())
                                                .itemId(item.itemId())
                                                .build())
                                .toList();
                OrderEntity newOrder = OrderEntity.builder()
                                .consumerId(checkoutRequest.consumerId())
                                .value(checkoutRequest.amount())
                                .paymentType(checkoutRequest.payment_method().type())
                                .paymentStatus(OrderStatus.PENDING)
                                .items(itemsEntities)
                                .currency(checkoutRequest.currency())
                                .fieldsNumber(checkoutRequest.payment_method().fields().number())
                                .fieldsExpirationMonth(checkoutRequest.payment_method().fields().expiration_month())
                                .fieldsExpirationYear(checkoutRequest.payment_method().fields().expiration_year())
                                .cvv(checkoutRequest.payment_method().fields().cvv())
                                .fieldsName(checkoutRequest.payment_method().fields().name())
                                .build();
                OrderEntity savedOrder = orderRepository.save(newOrder);
                return new BeforeSave(savedOrder.getId(), savedOrder.getPaymentStatus());
        }

        public OrderInfoList findByConsumerId(UUID consumerId) {
                List<OrderEntity> orders = orderRepository.findByConsumerId(consumerId);
                if (orders.isEmpty()) {
                        throw new BadRequestException("Não existem ordens deste usuário");
                }
                List<OrderInfo> orderInfo = orders.stream()
                                .map(o -> new OrderInfo(
                                                o.getId(),
                                                o.getItems()
                                                                .stream()
                                                                .map(i -> new Item(i.getItemId(), i.getQnt()))
                                                                .toList(),
                                                o.getPaymentType(),
                                                o.getValue(),
                                                o.getPaymentStatus()))
                                .toList();
                return new OrderInfoList(orderInfo);
        }

        public OrderInfo findByOrderId(UUID orderId) {
                Optional<OrderEntity> optOrder = orderRepository.findById(orderId);
                if (optOrder.isEmpty()) {
                        throw new BadRequestException("Não existe esta ordem");
                }
                OrderEntity order = optOrder.get();
                return new OrderInfo(
                                order.getId(),
                                order.getItems()
                                                .stream()
                                                .map(i -> new Item(i.getItemId(), i.getQnt())).toList(),
                                order.getPaymentType(),
                                order.getValue(),
                                order.getPaymentStatus());
        }
}
