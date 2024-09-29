package br.com.fiap.postech.adjt.checkout.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Checkout {

    @Id
    private UUID consumerId;

    private UUID orderId;

    private Integer amount;

    private Currency currency;

    @Embedded
    private PaymentMethod paymentMethod;

    private PaymentStatus status;

}