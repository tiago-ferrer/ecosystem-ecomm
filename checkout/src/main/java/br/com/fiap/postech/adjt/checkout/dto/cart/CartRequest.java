package br.com.fiap.postech.adjt.checkout.dto.cart;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartRequest {

    private String consumerId;
    private Long itemId;
    private Integer quantity;

    @Override
    public String toString() {
        return "CartRequest\n" +
                "consumerId: '" + consumerId + '\'' +
                "itemId:" + itemId +
                "quantity:" + quantity;
    }
}
