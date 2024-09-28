package br.com.fiap.postech.adjt.cart.infrastructure.adapters.input.rest.data.response;

import java.util.List;

import br.com.fiap.postech.adjt.cart.domain.model.ItemCart;
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
public class CartResponse {
    private List<ItemCart> items;
}
