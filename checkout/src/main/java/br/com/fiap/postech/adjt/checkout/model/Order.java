package br.com.fiap.postech.adjt.checkout.model;

import br.com.fiap.postech.adjt.checkout.dto.ItemDTO;
import br.com.fiap.postech.adjt.checkout.dto.OrderDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private List<Item> items;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private PaymentMethodType paymentMethodType;

    private Integer value;
    private PaymentStatus paymentStatus;


    public OrderDTO toDTO(Order order) {
        return new OrderDTO(
                order.getOrderId(),
                order.getConsumerId().toString(),
                order.getItems().stream()
                        .map(item -> new ItemDTO(item.getItemId(), item.getQnt()))
                        .collect(Collectors.toList()),
                order.getCurrency().toString(),
                order.getPaymentMethodType(),
                order.getValue(),
                order.getPaymentStatus()
        );
    }
}
