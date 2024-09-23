package br.com.fiap.postech.adjt.checkout.domain.model.order;

import br.com.fiap.postech.adjt.checkout.domain.model.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OrderModel {
    private UUID orderId;
    private PaymentStatus paymentStatus;
    private String consumerId;
    private String paymentType;
    private Double value;
    private List<OrderItemModel> items;
}