package br.com.fiap.postech.adjt.checkout.dto.payment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto  {

    @NotBlank(message = "Invalid payment information")
    private String type;

    @Valid
    private FieldDto fields;
}
