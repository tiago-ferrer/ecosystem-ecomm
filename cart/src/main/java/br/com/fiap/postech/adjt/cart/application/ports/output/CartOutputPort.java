package br.com.fiap.postech.adjt.cart.application.ports.output;

import java.util.Optional;
import java.util.UUID;

import br.com.fiap.postech.adjt.cart.domain.model.ItemCart;

public interface CartOutputPort {
    
    ItemCart saveProduct(ItemCart product);
    
    Optional<ItemCart> getProductById(UUID id);
    
}
