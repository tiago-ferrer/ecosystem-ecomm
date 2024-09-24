package br.com.fiap.postech.adjt.checkout.model.request;

import java.util.Map;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
	@NotEmpty
    private String orderId;
    @NotEmpty
    private double amount;
    @NotEmpty
    private String currency;
    @NotEmpty
    private PaymentMethod payment_method;

    @Data
    public static class PaymentMethod {
        private String type;
        private Map<String, String> fields;
    }
}