package br.com.fiap.postech.adjt.checkout.model.dto.response;

import java.util.List;
import java.util.UUID;

public record OrderCheckoutsResponse(
        UUID orderId,
        List<CartItemResponse> items,
        String paymentType,
        double value,
        String paymentStatus
) {
}
