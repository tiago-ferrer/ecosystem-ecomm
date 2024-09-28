package br.com.fiap.postech.adjt.cart.infrastructure.adapters.input.rest.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemsCartByCustomerIdResponse {

	private String itemId;
	
	@JsonProperty("qnt")
	private Integer quantity;

}
