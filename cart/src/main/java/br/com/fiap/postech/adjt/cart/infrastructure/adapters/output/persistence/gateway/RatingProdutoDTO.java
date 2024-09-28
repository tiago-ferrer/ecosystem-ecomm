package br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.gateway;

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
public class RatingProdutoDTO {
	
    public double rate;
    public int count;

}
