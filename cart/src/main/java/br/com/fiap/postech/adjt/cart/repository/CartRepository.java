package br.com.fiap.postech.adjt.cart.repository;

import br.com.fiap.postech.adjt.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByConsumerId(UUID consumerId);

}
