package br.com.fiap.postech.adjt.checkout.domain.model.order;

import br.com.fiap.postech.adjt.checkout.domain.model.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OrderStatusModel {
    private String paymentId;
    private PaymentStatus status;
}
