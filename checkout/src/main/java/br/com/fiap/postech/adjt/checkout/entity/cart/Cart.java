package br.com.fiap.postech.adjt.checkout.entity.cart;

import br.com.fiap.postech.adjt.checkout.dto.cart.CartDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
@Entity
@Table(name = "TB_CARTS")
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CART_ID")
    private UUID cartId;

    @Column(name = "CONSUMER_ID")
    private UUID consumerId;

    @OneToMany(mappedBy = "cartId", fetch = FetchType.LAZY)
    private List<Item> items = new ArrayList<>();

    public Cart(UUID cartId) {
        this.cartId = cartId;
    }

    public Cart(CartDto cartDto, UUID consumerId) {
        this.consumerId = consumerId;
        this.items = Optional.ofNullable(cartDto)
                .map(CartDto::getItems).map(items -> items.stream().map(Item::new).toList())
                .orElse(List.of());
    }

}
