package br.com.fiap.postech.adjt.checkout.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {

    @JsonProperty("id") // Certifique-se de que o nome do campo corresponde ao JSON
    private Long id; // ID do carrinho

    @JsonProperty("customerId") // Certifique-se de que o nome do campo corresponde ao JSON
    private String customerId; // ID do cliente

    @JsonProperty("items") // Certifique-se de que o nome do campo corresponde ao JSON
    private List<CartItemResponse> items; // Lista de itens do carrinho

}
