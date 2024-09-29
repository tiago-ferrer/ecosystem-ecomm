package br.com.fiap.postech.adjt.cart.repository;

import br.com.fiap.postech.adjt.cart.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findByConsumerId(UUID consumerId);
}
