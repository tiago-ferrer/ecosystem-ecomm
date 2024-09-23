package br.com.fiap.postech.adjt.checkout.dataprovider.client.gateway;

import br.com.fiap.postech.adjt.checkout.domain.gateway.CartGateway;
import br.com.fiap.postech.adjt.checkout.domain.model.cart.CartModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CartGatewayClientImpl implements CartGateway {

    @Value("${app.cart-base-url}")
    private String cartBaseUrl;

    @Override
    public CartModel getCartByConsumerId(String consumerId) {

        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Custom-Header", "custom-header-value");

        String body = "{ \"consumerId\": \"" + consumerId + "\" }";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<CartModel> response = client.exchange(cartBaseUrl, HttpMethod.GET, entity, CartModel.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {}
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
