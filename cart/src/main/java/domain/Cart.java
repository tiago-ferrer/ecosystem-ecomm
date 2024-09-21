package domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private UUID consumerId;
    private List<Item> items = new ArrayList<>();

    // Construtor adicional se necessário
    public Cart(UUID consumerId) {
        this.consumerId = consumerId;
        this.items = new ArrayList<>();
    }
}