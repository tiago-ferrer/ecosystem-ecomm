package br.com.fiap.postech.adjt.checkout.dataprovider.database.repository;

import br.com.fiap.postech.adjt.checkout.dataprovider.database.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    List<OrderEntity> findByConsumerId(String customerId);

    Optional<OrderEntity> findByOrderId(UUID orderId);
}
