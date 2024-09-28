package br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.entity;

import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(value = "cart")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartEntity {

	@Id
	private String id;
	
	private UUID consumerId;

	private List<ItemCartEntity> itemsCart;

}
