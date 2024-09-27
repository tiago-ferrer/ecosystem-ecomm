package br.com.fiap.postech.adjt.checkout.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import br.com.fiap.postech.adjt.checkout.entity.enums.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequestDto {

    @NotBlank
    @Pattern(
            regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "Invalid orderId format"
          )
    private String orderId;
    @NotBlank
    @Pattern(
            regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "Invalid consumerId format"
    )
    private String consumerId;
    private String paymentType;
    private int value;
    private PaymentStatus paymentStatus;

      public OrderRequestDto(String consumerId, String paymentType, int value, PaymentStatus paymentStatus) {
        this.consumerId = consumerId;
        this.paymentType = paymentType;
        this.value = value;
        this.paymentStatus = paymentStatus;
    }
}
