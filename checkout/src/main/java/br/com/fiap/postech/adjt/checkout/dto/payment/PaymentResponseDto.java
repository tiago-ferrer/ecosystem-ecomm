package br.com.fiap.postech.adjt.checkout.dto.payment;

import br.com.fiap.postech.adjt.checkout.exception.InvalidOrderIdException;
import lombok.Data;
import lombok.ToString;

import java.util.regex.Pattern;

@Data
@ToString
public class PaymentResponseDto {
    private String paymentId;
    private String status;

    public void setPaymentId(String paymentId) {
        if(isValidUUID(paymentId))
            this.paymentId = paymentId;
        else
            throw new InvalidOrderIdException("Invalid orderId format");
    }

    public boolean isValidUUID(String uuid) {
        return Pattern.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", uuid);
    }


}
