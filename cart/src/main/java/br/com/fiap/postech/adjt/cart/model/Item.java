package br.com.fiap.postech.adjt.cart.model;


import br.com.fiap.postech.adjt.cart.dto.CartRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_ITEMS")
public class Item {

    @Id
    private ItemColumnsId id;

    @Column(name = "QNT")
    private Integer qnt;


    public Item(CartRequest cartRequest) {
        this.id = new ItemColumnsId(UUID.fromString(cartRequest.getConsumerId()), Long.parseLong(cartRequest.getItemId()));
        this.qnt = cartRequest.getQuantity();
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class ItemColumnsId {

        @Column(name = "CONSUMER_ID")
        private UUID consumerId;

        @Column(name = "ITEM_ID")
        private Long itemId;

    }

}
