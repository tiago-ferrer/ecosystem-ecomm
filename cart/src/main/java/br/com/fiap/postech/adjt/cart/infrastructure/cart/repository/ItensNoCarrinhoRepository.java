package br.com.fiap.postech.adjt.cart.infrastructure.cart.repository;

import br.com.fiap.postech.adjt.cart.infrastructure.cart.model.ItensNoCarrinhoEntity;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.model.ItensNoCarrinhoId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItensNoCarrinhoRepository extends JpaRepository<ItensNoCarrinhoEntity, ItensNoCarrinhoId> {

    List<ItensNoCarrinhoEntity> findByIdIdCarrinho(final Long idCarrinho);

}
