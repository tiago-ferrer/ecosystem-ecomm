package br.com.fiap.postech.adjt.checkout.domain.model.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItensModel {
    private String itemId;
    private Long quantity;
}
