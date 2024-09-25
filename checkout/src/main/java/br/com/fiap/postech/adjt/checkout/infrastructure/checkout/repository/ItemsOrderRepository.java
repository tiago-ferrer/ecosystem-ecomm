package br.com.fiap.postech.adjt.checkout.infrastructure.checkout.repository;

import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.model.ItemsOrderEntity;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.model.ItemsOrderId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemsOrderRepository extends JpaRepository<ItemsOrderEntity, ItemsOrderId> {

}
