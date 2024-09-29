package br.com.fiap.postech.adjt.checkout.infrastructure.checkout.repository;

import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.model.OrderAsyncEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderAsyncRepository extends JpaRepository<OrderAsyncEntity, UUID> {

}
