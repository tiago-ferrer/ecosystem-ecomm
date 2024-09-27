package br.com.fiap.postech.adjt.checkout.mapper;

import java.util.List;
import java.util.stream.Collectors;

import br.com.fiap.postech.adjt.checkout.model.CartItemEntity;
import br.com.fiap.postech.adjt.checkout.model.OrderEntity;
import br.com.fiap.postech.adjt.checkout.model.dto.response.CartResponse;
import br.com.fiap.postech.adjt.checkout.model.dto.response.OrderCheckoutsResponse;

public class OrderMapper {

    // Método para converter um único OrderEntity para OrderCheckoutsResponse
    public static OrderCheckoutsResponse toResponse(OrderEntity orderEntity) {
        // Converte a lista de CartItemEntity para CartResponse
        List<CartResponse> items = orderEntity.getItems().stream()
            .map(OrderMapper::convertCartItemToResponse)
            .collect(Collectors.toList());

        return new OrderCheckoutsResponse(
            orderEntity.getOrderId().toString(), // Converte UUID para String
            items,
            orderEntity.getPaymentType(),
            orderEntity.getValue(),
            orderEntity.getPaymentStatus()
        );
    }

    // Método para converter uma lista de OrderEntity para uma lista de OrderCheckoutsResponse
    public static List<OrderCheckoutsResponse> toResponseList(List<OrderEntity> orderEntities) {
        return orderEntities.stream()
            .map(OrderMapper::toResponse)
            .collect(Collectors.toList());
    }

    // Método auxiliar para converter CartItemEntity para CartResponse
    private static CartResponse convertCartItemToResponse(CartItemEntity cartItemEntity) {
//        return new CartResponse(
//            cartItemEntity.getItemId(),
//            cartItemEntity.getId(),
//            null
//        );
    	return null;
    }
    
}