package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.model.Cart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.UUID;

@Service
public class CartService {

    private final RestTemplate restTemplate;
    private final String cartServiceUrl;

    public CartService(RestTemplate restTemplate, @Value("${cart.service.url}") String cartServiceUrl) {
        this.restTemplate = restTemplate;
        this.cartServiceUrl = cartServiceUrl;
    }

    public Cart getCart(UUID consumerId) {
        String url = cartServiceUrl + "/" + consumerId;
        return restTemplate.getForObject(url, Cart.class);
    }

    public void clearCart(UUID consumerId) {
        String url = cartServiceUrl + "/" + consumerId;
        restTemplate.delete(url);
    }
}
