package br.com.fiap.postech.adjt.checkout.infrastructure.persistance.gateways;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.*;
import br.com.fiap.postech.adjt.checkout.infrastructure.persistance.repositories.ItemRepository;
import org.springframework.stereotype.Service;

import br.com.fiap.postech.adjt.checkout.domain.entities.Item;
import br.com.fiap.postech.adjt.checkout.domain.entities.enums.OrderStatus;
import br.com.fiap.postech.adjt.checkout.infrastructure.controllers.exceptions.BadRequestException;
import br.com.fiap.postech.adjt.checkout.infrastructure.persistance.entities.ItemEntity;
import br.com.fiap.postech.adjt.checkout.infrastructure.persistance.entities.OrderEntity;
import br.com.fiap.postech.adjt.checkout.infrastructure.persistance.repositories.OrderRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderGateway {
        private final OrderRepository orderRepository;
        private final ItemRepository itemRepository;

        public BeforeSave checkout(CheckoutRequest checkoutRequest, List<Item> items) {
                OrderEntity newOrder = OrderEntity.builder()
                                .consumerId(checkoutRequest.consumerId())
                                .value(checkoutRequest.amount())
                                .paymentType(checkoutRequest.payment_method().type())
                                .paymentStatus(OrderStatus.pending)
                                .currency(checkoutRequest.currency())
                                .fieldsNumber(checkoutRequest.payment_method().fields().number())
                                .fieldsExpirationMonth(checkoutRequest.payment_method().fields().expiration_month())
                                .fieldsExpirationYear(checkoutRequest.payment_method().fields().expiration_year())
                                .cvv(checkoutRequest.payment_method().fields().cvv())
                                .fieldsName(checkoutRequest.payment_method().fields().name())
                                .build();
                OrderEntity savedOrder = orderRepository.save(newOrder);
                List<ItemEntity> itemsEntities = items.stream()
                                .map(item -> ItemEntity.builder()
                                                .qnt(item.qnt())
                                                .itemId(item.itemId())
                                                .order(savedOrder)
                                                .build())
                                .toList();
                itemRepository.saveAll(itemsEntities);
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
                        .map(item -> new ItemEntity(item.itemId(), item.qnt(), order))
                        .toList();
                order.setItems(itemsEntities);
                orderRepository.save(order);
        }

        public PaymentRequest findById(UUID orderId) throws BadRequestException {
                Optional<OrderEntity> order = orderRepository.findById(orderId);
                if (order.isEmpty()) throw new BadRequestException("Ordem não encontrada");
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
