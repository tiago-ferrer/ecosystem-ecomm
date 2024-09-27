package br.com.fiap.postech.adjt.payment.infrastructure.persistance.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
    @ManyToOne
    private String itemId;

    private Integer quantity;
}
