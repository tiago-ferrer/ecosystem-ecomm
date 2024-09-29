package br.com.fiap.postech.adjt.checkout.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.postech.adjt.checkout.model.Order;

public interface OrderRepository extends JpaRepository<Order, UUID> {
	
	List<Order> findByConsumerId(UUID consumerId);

	List<Order> findByPaymentStatus(String string);
}