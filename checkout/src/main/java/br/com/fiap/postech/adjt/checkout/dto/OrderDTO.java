package br.com.fiap.postech.adjt.checkout.dto;

import java.util.List;
import java.util.UUID;

public record OrderDTO(UUID orderId, List<OrderItemDTO> items, String paymentType, double value, String paymentStatus) { }