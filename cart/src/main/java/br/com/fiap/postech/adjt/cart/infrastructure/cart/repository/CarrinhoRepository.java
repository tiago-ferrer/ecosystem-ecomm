package br.com.fiap.postech.adjt.cart.infrastructure.cart.repository;

import br.com.fiap.postech.adjt.cart.domain.cart.StatusEnum;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.model.CarrinhoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarrinhoRepository extends JpaRepository<CarrinhoEntity, Long> {

    Optional<CarrinhoEntity> findByUsuarioAndStatus(final String token,
                                                    final StatusEnum status);

}
