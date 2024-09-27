package br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.entity.ItemCartEntity;

public interface ItemCartRepository extends JpaRepository<ItemCartEntity, UUID>{
    
}
