package br.com.fiap.postech.adjt.cart.domain.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

	private String consumerId;

	private List<ItemCart> itemsCart;
}
