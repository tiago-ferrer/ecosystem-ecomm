package br.com.fiap.postech.adjt.checkout.domain.model.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartModel {
    List<CartItensModel> items;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static public class CartItensModel {
        private String itemId;
        private Long quantity;
    }
}
