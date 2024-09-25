package br.com.fiap.postech.adjt.checkout.dto;

import br.com.fiap.postech.adjt.checkout.entity.PaymentStatus;

public record PaymentDTO(
        String orderId,
        PaymentStatus status
) { }
