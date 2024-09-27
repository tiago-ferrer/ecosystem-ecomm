package br.com.fiap.postech.adjt.checkout.model;

import br.com.fiap.postech.adjt.checkout.dto.OrderResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;

    private UUID consumerId;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Item> itemList;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private PaymentMethodType paymentMethodType;

    private Double totalValue;
    private PaymentStatus paymentStatus;

    public OrderResponseDTO toDTO() {
        return new OrderResponseDTO(this);
    }
}
