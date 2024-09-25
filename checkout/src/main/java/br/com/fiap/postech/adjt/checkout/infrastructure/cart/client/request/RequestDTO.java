package br.com.fiap.postech.adjt.checkout.infrastructure.cart.client.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class RequestDTO {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	String consumerId;

}
