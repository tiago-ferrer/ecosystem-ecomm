package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.dto.order.OrderRequestDto;
import br.com.fiap.postech.adjt.checkout.dto.order.OrderResponseDto;
import br.com.fiap.postech.adjt.checkout.entity.enums.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.entity.order.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {
     Order createOrder(OrderRequestDto order);
     OrderResponseDto getById(UUID id);
     OrderResponseDto retrieveById(UUID id);
     List<Order> getOrdersByConsumer(UUID consumerId);
     void updateStatus(OrderResponseDto order);

     void updateStatusByStatusName(UUID orderId, PaymentStatus paymentStatus);
}
