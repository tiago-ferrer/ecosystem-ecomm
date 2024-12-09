package br.com.fiap.postech.adjt.checkout.dataprovider.client.gateway;

import br.com.fiap.postech.adjt.checkout.domain.gateway.CartGateway;
import br.com.fiap.postech.adjt.checkout.domain.model.cart.CartItensModel;
import br.com.fiap.postech.adjt.checkout.domain.model.cart.CartModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartGatewayClientImpl implements CartGateway {
    @Override
    public CartModel getCartByConsumerId(String consumerId) {
        /*
            TODO criar integracao com carrinho
         */
        return new CartModel(List.of(new CartItensModel("137453ac-21c3-4e43-a652-7e2867fcb6d1", 1L)));
    }
}
