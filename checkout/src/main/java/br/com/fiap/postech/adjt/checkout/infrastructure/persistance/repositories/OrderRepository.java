package br.com.fiap.postech.adjt.checkout.infrastructure.persistance.repositories;


import br.com.fiap.postech.adjt.checkout.infrastructure.persistance.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    List<OrderEntity> findByConsumerId(UUID id);
}