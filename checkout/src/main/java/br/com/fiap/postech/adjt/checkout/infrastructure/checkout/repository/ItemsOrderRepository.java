package br.com.fiap.postech.adjt.checkout.infrastructure.checkout.repository;

import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.model.ItemsOrderEntity;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.model.ItemsOrderId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ItemsOrderRepository extends JpaRepository<ItemsOrderEntity, ItemsOrderId> {

    List<ItemsOrderEntity> findByIdIdOrder(final UUID idOrder);

}
