package br.com.fiap.postech.adjt.cart.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class CartConsumerRequest {
    private UUID consumerId;
}
