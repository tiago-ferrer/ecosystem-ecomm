package br.com.fiap.postech.adjt.checkout.infrastructure.persistance.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Table(name = "items")
@Entity(name = "items")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Slf4j
public class ItemEntity {
    @Id
    private Integer itemId;
    private Integer qnt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false) // Define a chave estrangeira
    private OrderEntity order;
}
