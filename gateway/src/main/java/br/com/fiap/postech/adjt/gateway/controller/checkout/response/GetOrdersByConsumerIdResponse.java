package br.com.fiap.postech.adjt.gateway.controller.checkout.response;

import java.util.List;

public record GetOrdersByConsumerIdResponse(
        List<OrderResponse> orders
) { }
