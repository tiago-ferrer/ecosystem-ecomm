package br.com.fiap.postech.adjt.checkout.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    @JsonProperty("id") // Certifique-se de que o nome do campo corresponde ao JSON
    private Long id; // ID do item no carrinho

    @JsonProperty("itemId") // Certifique-se de que o nome do campo corresponde ao JSON
    private Long itemId; // ID do produto

    @JsonProperty("quantity") // Certifique-se de que o nome do campo corresponde ao JSON
    private Integer quantity; // Quantidade do produto

	public CartItemResponse(Long itemId, Integer quantity) {
		super();
		this.itemId = itemId;
		this.quantity = quantity;
	}
   
}
