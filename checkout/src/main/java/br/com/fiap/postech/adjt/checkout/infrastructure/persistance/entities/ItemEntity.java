package br.com.fiap.postech.adjt.checkout.infrastructure.persistance.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Table(name = "items")
@Entity(name = "items")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ItemEntity {
    @Id
    private Integer itemId;
    private Integer quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false) // Define a chave estrangeira
    private OrderEntity order;
}
