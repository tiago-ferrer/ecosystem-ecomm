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
@Table(name = "tb_order_async")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderAsyncEntity {

    @Id
    private UUID id;
    private String consumerId;
    private Long amount;
    private String currency;
    private String paymentMethodType;
    private String fieldNumber;
    private String fieldExpirationMonth;
    private String fieldExpirationYear;
    private String fieldCvv;
    private String fieldName;
    private LocalDateTime dataDeCriacao;

}
