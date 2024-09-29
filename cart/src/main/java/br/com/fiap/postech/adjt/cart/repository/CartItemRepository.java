package br.com.fiap.postech.adjt.cart.repository;

import br.com.fiap.postech.adjt.cart.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByConsumerIdAndItemId(UUID consumerId, Long itemId);

    List<Item> findByCart_CartId(Long cartId);
}
