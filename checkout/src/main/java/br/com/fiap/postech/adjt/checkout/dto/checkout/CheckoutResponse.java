package br.com.fiap.postech.adjt.checkout.dto.checkout;

import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentRequestDto;
import br.com.fiap.postech.adjt.checkout.entity.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutResponse {
    private String orderId;
    private String status;

    public CheckoutResponse(PaymentRequestDto paymentRequest) {
        this.orderId = paymentRequest.getOrderId().toString();
        this.status = PaymentStatus.pending.toString();
    }

}
