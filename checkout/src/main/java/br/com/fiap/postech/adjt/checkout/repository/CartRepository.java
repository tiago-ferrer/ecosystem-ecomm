package br.com.fiap.postech.adjt.checkout.repository;

import br.com.fiap.postech.adjt.checkout.entity.cart.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {

    @EntityGraph(attributePaths = "items", type = EntityGraph.EntityGraphType.FETCH)
//    @Query(value = "select * from TB_CARTS where = :consumerId")
    Optional<Cart> findByConsumerId(@Param("consumerId") UUID consumerId);

}
