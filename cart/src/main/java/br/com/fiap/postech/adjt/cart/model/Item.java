package br.com.fiap.postech.adjt.cart.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @NotNull
    private String productId;
    @Min(1)
    private int quantity;
}
