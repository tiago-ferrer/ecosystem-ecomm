package br.com.fiap.postech.adjt.checkout.model;

import java.util.UUID;

public class Checkout {
    private UUID orderId;
    private UUID customerId;
    private Double amount;
    private Currency currency;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;


}
