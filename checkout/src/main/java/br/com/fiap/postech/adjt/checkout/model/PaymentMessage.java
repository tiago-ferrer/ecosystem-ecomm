package br.com.fiap.postech.adjt.checkout.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMessage {
    private Order order;
    private Checkout checkout;
}
