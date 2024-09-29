package br.com.fiap.postech.adjt.checkout.repository;

import br.com.fiap.postech.adjt.checkout.entity.cart.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
