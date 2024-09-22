package br.com.fiap.postech.adjt.cart.model;

import org.springframework.data.annotation.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    private UUID consumerId;
    private List<Item> items;
}
