package br.com.fiap.postech.adjt.payment.infrastructure.persistance.repositories;

import br.com.fiap.postech.adjt.payment.infrastructure.persistance.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

}