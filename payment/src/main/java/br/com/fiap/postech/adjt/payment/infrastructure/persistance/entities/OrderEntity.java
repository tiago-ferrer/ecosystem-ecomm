package br.com.fiap.postech.adjt.payment.infrastructure.persistance.entities;


import br.com.fiap.postech.adjt.payment.domain.entities.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Table(name = "orders")
@Entity(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "consumer_id")
    private UUID consumerId;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemEntity> items;
    Integer value;
    @Column(name = "payment_type")
    String paymentType;
    @Column(name = "payment_status")
    OrderStatus paymentStatus;
    String currency;
    @Column(name = "fields_number")
    String fieldsNumber;
    @Column(name = "fields_expiration_month")
    String fieldsExpirationMonth;
    @Column(name = "fields_expiration_year")
    String fieldsExpirationYear;
    String cvv;
    String fieldsName;
}
