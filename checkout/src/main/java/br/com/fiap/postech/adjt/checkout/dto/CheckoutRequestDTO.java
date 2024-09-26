package br.com.fiap.postech.adjt.checkout.dto;

import java.util.List;
import java.util.UUID;

public record CheckoutRequestDTO( UUID consumerId,
                                  Double amount,
                                  String currency,
                                  PaymentMethodDTO paymentMethod,
                                  List<CartItemDTO> items) {
}
