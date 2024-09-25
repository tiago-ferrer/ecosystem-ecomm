package br.com.fiap.postech.adjt.checkout.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutResponse {
	private String orderId;
	private String status;
}