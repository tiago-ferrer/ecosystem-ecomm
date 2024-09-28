package br.com.fiap.postech.adjt.checkout.controller.order;

import br.com.fiap.postech.adjt.checkout.controller.order.OrderResponse;

import java.util.List;

public record GetOrdersByConsumerIdResponse(
        List<OrderResponse> orders
) { }
