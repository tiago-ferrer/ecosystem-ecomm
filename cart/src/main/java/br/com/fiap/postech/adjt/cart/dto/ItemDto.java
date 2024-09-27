package br.com.fiap.postech.adjt.cart.dto;

import br.com.fiap.postech.adjt.cart.model.Item;
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
        this.itemId = item.getId().getItemId();
        this.qnt = item.getQnt();
    }

}
