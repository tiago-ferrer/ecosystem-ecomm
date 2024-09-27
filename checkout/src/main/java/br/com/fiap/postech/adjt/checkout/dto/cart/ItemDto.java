package br.com.fiap.postech.adjt.checkout.dto.cart;

import br.com.fiap.postech.adjt.checkout.entity.cart.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private Long itemId;
    private Integer qnt;

    public ItemDto(Item item) {
        this.itemId = item.getItemId();
        this.qnt = item.getQnt();
    }


}
