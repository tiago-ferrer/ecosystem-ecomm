package br.com.fiap.postech.adjt.checkout.infrastructure.checkout.repository;

import br.com.fiap.postech.adjt.checkout.domain.checkout.StatusPagamento;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    Optional<OrderEntity> findByUsuarioAndStatus(final String consumerId,
                                                 final StatusPagamento status);

}
