package br.com.fiap.postech.adjt.checkout.dataprovider.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Entity(name = "order_items")
@AllArgsConstructor
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "cod_item", nullable = false)
    private String codItem;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "price")
    private Double price;

    @ManyToOne
    @JoinColumn(name="order_id")
    private OrderEntity order;

}

