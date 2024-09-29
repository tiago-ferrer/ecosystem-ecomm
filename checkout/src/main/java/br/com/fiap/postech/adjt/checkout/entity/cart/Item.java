package br.com.fiap.postech.adjt.checkout.entity.cart;

import br.com.fiap.postech.adjt.checkout.dto.cart.ItemDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_ITEMS")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ITEM_ID")
    private Long itemId;

    @Column(name = "QNT")
    private Integer qnt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CART_ID", referencedColumnName = "CART_ID")
    private Cart cartId;


    /*public Item(CartRequest cartRequest) {
        this.id =  cartRequest.getItemId();
        this.consumerId = new Cart(UUID.fromString(cartRequest.getConsumerId()));
        this.qnt = cartRequest.getQuantity();
    }
*/

    public Item(Integer qnt, Cart cart) {
        this.cartId = cart;
        this.qnt = qnt;
    }

    public Item(ItemDto itemDto) {
        this.itemId = itemDto.getItemId();
        this.qnt = itemDto.getQnt();
    }
}
