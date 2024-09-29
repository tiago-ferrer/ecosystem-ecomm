package br.com.fiap.postech.adjt.checkout.domain.order;

import br.com.fiap.postech.adjt.checkout.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByConsumerId(UUID customerId);
    Order findByOrderId(UUID orderId);
}
