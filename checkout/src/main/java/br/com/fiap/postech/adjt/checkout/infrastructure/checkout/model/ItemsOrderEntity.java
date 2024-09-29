package br.com.fiap.postech.adjt.checkout.infrastructure.checkout.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_order_itens")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemsOrderEntity {

    @EmbeddedId
    private ItemsOrderId id;
    private Long quantidade;

}
