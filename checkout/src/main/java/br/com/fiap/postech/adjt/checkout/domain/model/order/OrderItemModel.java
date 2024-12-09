package br.com.fiap.postech.adjt.checkout.domain.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemModel {
    private UUID id;
    private Long quantity;
    private String codItem;
//    private Double price;
}

