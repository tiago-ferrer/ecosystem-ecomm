package br.com.fiap.postech.adjt.checkout.model;

import lombok.Data;

@Data

public class PaymentMessage {
    private Order order;
    private Checkout checkout;
}
