package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.model.Cart;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

@Service
public class CartService {

    private final WebClient webClient;

    public CartService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081/cart").build();
    }

    public Cart getCartDetails(String orderId) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("").build(orderId))
                    .retrieve()
                    .bodyToMono(Cart.class)
                    .block();
        } catch (WebClientException e) {
            throw e;
        }
    }
}
