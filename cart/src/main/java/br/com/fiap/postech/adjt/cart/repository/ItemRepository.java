package br.com.fiap.postech.adjt.cart.repository;

import br.com.fiap.postech.adjt.cart.model.Item;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, Item.ItemColumnsId> {

    @Query(value = "select * from TB_ITEMS where CONSUMER_ID = :consumerId", nativeQuery = true)
    List<Item> findAllByConsumerId(@Param("consumerId") UUID consumerId);

    @Modifying
    @Transactional
    @Query(value = "delete from TB_ITEMS where CONSUMER_ID = :consumerId", nativeQuery = true)
    void deleteByConsumerId(@Param("consumerId") UUID consumerId);


}
