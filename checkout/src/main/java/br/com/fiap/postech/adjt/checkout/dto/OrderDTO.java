package br.com.fiap.postech.adjt.checkout.dto;

import br.com.fiap.postech.adjt.checkout.entity.PaymentStatus;

import java.util.List;

public record OrderDTO(
    String orderId,
    List<ItemDTO> items,
    String paymentType,
    Long value,
    PaymentStatus paymentStatus
) { }
