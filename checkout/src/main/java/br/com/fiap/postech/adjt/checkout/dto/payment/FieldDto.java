package br.com.fiap.postech.adjt.checkout.dto.payment;

import br.com.fiap.postech.adjt.checkout.entity.payment.Field;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldDto {

    @NotBlank(message = "Invalid payment information")
    private String number;

    @NotBlank(message = "Invalid payment information")
    private String expiration_month;

    @NotBlank(message = "Invalid payment information")
    private String expiration_year;

    @NotBlank(message = "Invalid payment information")
    private String cvv;

    @NotBlank(message = "Invalid payment information")
    private String name;

    public Field convertDto(FieldDto fieldDto) {

        Field field = new Field();
        field.setCvv(fieldDto.getCvv());
        field.setName(fieldDto.getName());
        field.setNumber(fieldDto.getNumber());
        field.setExpiration_month(fieldDto.getExpiration_month());
        field.setExpiration_year(fieldDto.getExpiration_year());
        return field;
    }
}
