package br.com.fiap.postech.adjt.checkout.repository;

import br.com.fiap.postech.adjt.checkout.entity.order.Order;
import br.com.fiap.postech.adjt.checkout.entity.enums.PaymentStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
//    List<Order> findByConsumerId(UUID consumerId);

    @EntityGraph(attributePaths = {"cart"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Order> findByConsumerId(UUID consumerId);

    @EntityGraph(attributePaths = "cart", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Order> findByOrderId(UUID orderId);
}
