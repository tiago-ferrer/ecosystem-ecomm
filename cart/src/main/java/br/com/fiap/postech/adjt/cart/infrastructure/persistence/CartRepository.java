package br.com.fiap.postech.adjt.cart.infrastructure.persistence;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<CartEntity, UUID> {

}
