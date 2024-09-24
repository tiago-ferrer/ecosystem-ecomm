package br.com.fiap.postech.adjt.checkout.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Checkout {

    private UUID orderId;
    @Id
    private UUID consumerId;
    private Double amount;
    private Currency currency;
    @OneToOne
    private PaymentMethod paymentMethod;
    private PaymentStatus status;

}
