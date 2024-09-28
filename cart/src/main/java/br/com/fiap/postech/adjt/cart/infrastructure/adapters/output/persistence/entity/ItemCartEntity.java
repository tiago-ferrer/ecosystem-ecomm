package br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCartEntity {
	
	@Id
	private String id;
	
	private String itemId;
	
	private Integer quantity;
	
	private CartEntity cart;
	
}
