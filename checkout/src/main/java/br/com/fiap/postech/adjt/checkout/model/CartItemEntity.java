package br.com.fiap.postech.adjt.checkout.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "item-cart")
public class CartItemEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private Long itemId;
    private int quantity;

    public CartItemEntity(Long itemId, int quantity) {
		super();
		this.itemId = itemId;
		this.quantity = quantity;
	}
}