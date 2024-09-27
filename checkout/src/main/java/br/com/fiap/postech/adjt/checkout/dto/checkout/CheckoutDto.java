package br.com.fiap.postech.adjt.checkout.dto.checkout;

import br.com.fiap.postech.adjt.checkout.dto.payment.FieldDto;
import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentDto;
import br.com.fiap.postech.adjt.checkout.entity.checkout.Checkout;
import br.com.fiap.postech.adjt.checkout.entity.enums.PaymentStatus;
import br.com.fiap.postech.adjt.checkout.entity.payment.Field;
import br.com.fiap.postech.adjt.checkout.entity.payment.Payment;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.UUID;

@Data
public class CheckoutDto {

    @Pattern(
            regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "Invalid orderId format"
    )
    @NotNull(message = "Invalid payment information")
    private String consumerId;

    @Min(value = 1, message = "Invalid payment information")
    private int amount;

    @NotBlank(message = "Invalid payment information")
    private String currency;

    @Valid
    @NotNull(message = "Invalid payment information")
    private PaymentDto payment_method;

    private String paymentType;

    private PaymentStatus status;

    public CheckoutDto convertToDto(Checkout checkout) {
        ModelMapper mapper = new ModelMapper();
        FieldDto fieldDto = mapper.map(checkout.getPaymentMethod().getField(), FieldDto.class);

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setType(checkout.getPaymentType());
        paymentDto.setFields(fieldDto);

        CheckoutDto dto = new CheckoutDto();
        dto.setConsumerId(checkout.getConsumerId().toString());
        dto.setCurrency(checkout.getCurrency());
        dto.setAmount(checkout.getAmount());
        dto.setPayment_method(paymentDto);
        return dto;
    }

    public Checkout convertFromDto(CheckoutDto checkoutDto){

        ModelMapper mapper = new ModelMapper();
        Field field = mapper.map(checkoutDto.getPayment_method().getFields(), Field.class);
        Payment payment = mapper.map(checkoutDto.getPayment_method(), Payment.class);
        payment.setField(field);

        Checkout checkout = new Checkout();
        checkout.setValue(checkoutDto.getAmount());
        checkout.setCurrency(checkout.getCurrency());
        checkout.setAmount(checkoutDto.getAmount());
        checkout.setPaymentType(checkoutDto.getPaymentType());
        checkout.setPaymentMethod(payment);
        checkout.setConsumerId(UUID.fromString(checkoutDto.getConsumerId()));
        checkout.setPaymentStatus(checkoutDto.getStatus());
        return checkout;
    }

}
