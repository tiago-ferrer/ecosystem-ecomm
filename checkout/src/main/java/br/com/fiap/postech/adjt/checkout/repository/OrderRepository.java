package br.com.fiap.postech.adjt.checkout.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.postech.adjt.checkout.model.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
	
	List<OrderEntity> findByConsumerId(UUID consumerId);

	List<OrderEntity> findByPaymentStatus(String string);
}