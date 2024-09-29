package br.com.fiap.postech.adjt.gateway.checkout.response;

import java.util.List;

public record GetOrdersByConsumerIdResponse(
        List<OrderResponse> orders
) { }
