package br.com.fiap.postech.adjt.checkout.domain.checkout.dto;

import br.com.fiap.postech.adjt.checkout.domain.checkout.PaymentStatus;

public record PaymentDTO(
        String orderId,
        PaymentStatus status
) { }
