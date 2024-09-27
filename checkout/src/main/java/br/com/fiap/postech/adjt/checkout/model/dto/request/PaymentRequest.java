package br.com.fiap.postech.adjt.checkout.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    private int amount;
    
    @NotBlank(message = "Currency cannot be blank")
    private String currency;

    @NotNull(message = "Payment method cannot be null")
    @JsonProperty("payment_method")
    private PaymentMethodRequest payment_method;
}