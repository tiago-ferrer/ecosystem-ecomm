package br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.entity.CartEntity;

@Repository
public interface CartRepository extends MongoRepository<CartEntity, String>{

	Optional<CartEntity> findByConsumerId(UUID consumerId);
    
}
