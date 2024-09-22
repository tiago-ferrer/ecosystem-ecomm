package br.com.fiap.postech.adjt.checkout.model;

import java.util.List;
import java.util.UUID;

public class Order {
    private UUID orderId;
    private List<Item> itemList;
    private PaymentMethodType paymentMethodType;
    private Double value;
    private PaymentStatus paymentStatus;
}
