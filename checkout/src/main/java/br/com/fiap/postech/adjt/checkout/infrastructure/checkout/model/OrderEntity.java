package br.com.fiap.postech.adjt.checkout.infrastructure.checkout.model;

import br.com.fiap.postech.adjt.checkout.domain.checkout.StatusPagamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_order")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    @Id
    private UUID id;
    private String usuario;
    private String paymentType;
    private BigDecimal orderValue;
    @Enumerated(EnumType.STRING)
    private StatusPagamento status;
    private LocalDateTime dataDeCriacao;

}
