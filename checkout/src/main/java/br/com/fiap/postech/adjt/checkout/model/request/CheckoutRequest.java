package br.com.fiap.postech.adjt.checkout.model.request;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutRequest {

    @NotBlank(message = "Consumer ID cannot be blank")
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid consumerId format")
    private UUID consumerId;

    @Min(value = 0, message = "Amount must be greater than or equal to 0")
    private double amount;

    @NotBlank(message = "Currency cannot be blank")
    private String currency;

    @NotNull(message = "Payment method cannot be null")
    @JsonProperty("payment_method")
    private PaymentRequest.PaymentMethod paymentMethod;

    // Custom validation for UUID format
    public void validateConsumerId() {
//        try {
//            UUID.fromString(consumerId);
//        } catch (IllegalArgumentException e) {
//            throw new IllegalArgumentException("Invalid orderId format");
//        }
    }
}