package br.com.fiap.postech.adjt.checkout.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDto {
    private UUID orderId;
    private int amount;
    private String currency;

    private PaymentDto payment_method;

    @Override
    public String toString() {
        return "CheckoutPaymentDto\n{" +
                "\norderId:" + orderId +
                ",\namount:" + amount +
                ",\ncurrency:'" + currency + '\'' +
                ",\npayment_method:{" +
                "\n\ttype:" + payment_method.getType() +
                "\n\tfields: {" +
                "\n\t\tnumber: {" + payment_method.getFields().getNumber() +
                "\n\t\texpiration_month: {" + payment_method.getFields().getExpiration_month() +
                "\n\t\texpiration_year: {" + payment_method.getFields().getExpiration_year() +
                "\n\t\tcvv: " + payment_method.getFields().getCvv() +
                "\n\t\tname: " + payment_method.getFields().getName() +
                "\n\t\t}\n\t}\n}";
    }
}
