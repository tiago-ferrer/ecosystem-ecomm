package br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.entity.CartEntity;

public interface CartRepository extends JpaRepository<CartEntity, UUID>{

	Optional<CartEntity> findByConsumerId(UUID consumerId);
    
}
