package br.com.fiap.postech.adjt.checkout.domain.repository;

import br.com.fiap.postech.adjt.checkout.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByCustomerId(String customerId);
}
