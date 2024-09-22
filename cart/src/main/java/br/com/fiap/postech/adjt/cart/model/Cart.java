package br.com.fiap.postech.adjt.cart.model;

import org.springframework.data.annotation.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    private String consumerId;
    private List<Item> items;
}
