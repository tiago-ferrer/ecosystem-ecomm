package br.com.fiap.postech.adjt.cart.repository;

import br.com.fiap.postech.adjt.cart.domain.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends MongoRepository<Cart, UUID> {
    Optional<Cart> findByConsumerId(UUID consumerId);
}