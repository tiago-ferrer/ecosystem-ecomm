package br.com.fiap.postech.adjt.cart.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item_cart")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCartEntity {

	@Id
	@ManyToOne
	private String itemId;

	private Integer quantity;

}
