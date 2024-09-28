package br.com.fiap.postech.adjt.checkout.infrastructure.persistance.repositories;

import br.com.fiap.postech.adjt.checkout.infrastructure.persistance.entities.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemRepository extends JpaRepository<ItemEntity, UUID> {
}
