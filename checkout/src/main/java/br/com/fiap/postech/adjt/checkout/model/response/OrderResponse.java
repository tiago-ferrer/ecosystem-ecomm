package br.com.fiap.postech.adjt.checkout.model.response;

import java.util.List;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

	private String orderId;
	private List<Item> items;
	private String paymentType;
	private double value;
	private String paymentStatus;
}
