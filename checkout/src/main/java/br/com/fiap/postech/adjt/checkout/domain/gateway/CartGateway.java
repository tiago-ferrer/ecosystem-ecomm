package br.com.fiap.postech.adjt.checkout.domain.gateway;

import br.com.fiap.postech.adjt.checkout.domain.model.cart.CartModel;

public interface CartGateway {
    CartModel getCartByConsumerId(String consumerId);
}
