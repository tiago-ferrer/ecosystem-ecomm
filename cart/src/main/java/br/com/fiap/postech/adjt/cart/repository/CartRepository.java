package br.com.fiap.postech.adjt.cart.repository;

import br.com.fiap.postech.adjt.cart.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepository extends MongoRepository<Cart, String> {
    Cart findByConsumerId(String consumerId);
}
