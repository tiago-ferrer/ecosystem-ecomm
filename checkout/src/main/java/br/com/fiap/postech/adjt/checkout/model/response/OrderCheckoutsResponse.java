package br.com.fiap.postech.adjt.checkout.model.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCheckoutsResponse {

	private String orderId;
	private List<CartResponse> items;
	private String paymentType;
	private double value;
	private String paymentStatus;
}
