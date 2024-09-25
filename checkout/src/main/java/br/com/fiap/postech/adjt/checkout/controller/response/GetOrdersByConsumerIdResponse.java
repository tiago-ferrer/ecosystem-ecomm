package br.com.fiap.postech.adjt.checkout.controller.response;

import java.util.List;

public record GetOrdersByConsumerIdResponse(
        List<OrderResponse> orders
) { }
