package br.com.fiap.postech.adjt.checkout.model.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {

	private UUID customerId;
	private Long itemId;
	private Integer quantity;

	public CartResponse(Long itemId, Integer quantity) {
		super();
		this.itemId = itemId;
		this.quantity = quantity;
	}

}
