package br.com.fiap.postech.adjt.checkout.dto;

import java.util.UUID;

public record PaymentRequestDTO(UUID orderId, double amount, String currency) {}
