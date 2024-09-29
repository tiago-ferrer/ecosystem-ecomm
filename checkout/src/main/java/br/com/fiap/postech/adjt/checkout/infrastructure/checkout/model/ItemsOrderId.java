package br.com.fiap.postech.adjt.checkout.infrastructure.checkout.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemsOrderId {

    private UUID idOrder;
    private Long ean;

}
