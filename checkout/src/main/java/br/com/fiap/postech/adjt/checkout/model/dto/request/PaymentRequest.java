package br.com.fiap.postech.adjt.checkout.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
	@NotBlank(message = "Order ID cannot be blank")
    private String orderId;
    
	@Min(value = 1, message = "Amount must be greater than zero")
    private int amount;
    
	@NotBlank(message = "Currency cannot be blank")
    private String currency;

    @NotNull(message = "Payment method cannot be null")
    @JsonProperty("payment_method")
    private PaymentMethodRequest payment_method;
}