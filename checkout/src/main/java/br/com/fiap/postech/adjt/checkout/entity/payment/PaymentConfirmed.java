package br.com.fiap.postech.adjt.checkout.entity.payment;

import br.com.fiap.postech.adjt.checkout.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_payment_aux")
public class PaymentConfirmed {
    @Id
    private UUID paymentId;
    private UUID orderId;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

}
