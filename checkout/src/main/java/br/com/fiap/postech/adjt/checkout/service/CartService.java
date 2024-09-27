package br.com.fiap.postech.adjt.checkout.service;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import br.com.fiap.postech.adjt.checkout.dto.ConsumerIdRequest;
import br.com.fiap.postech.adjt.checkout.model.Cart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@Service
public class CartService {

    @Value("${cart.service.url}")
    private String cartServiceUrl;
    private final WebClient webClient;

    public CartService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://cart:8081").build();
    }

    @Cacheable(value = "cartCache", key = "#consumerId")
    public Cart getCart(UUID consumerId) {
        ConsumerIdRequest request = new ConsumerIdRequest(consumerId.toString());

        return webClient
                .method(HttpMethod.GET)
                .uri("/")
                .bodyValue(request)  // Adiciona o corpo na requisição GET
                .retrieve()
                .bodyToMono(Cart.class)
                .block();  // Converte a resposta para Mono<Cart>
    }

    @CacheEvict(value = "cartCache", key = "#consumerId")
    public Void clearCart(UUID consumerId) {
        ConsumerIdRequest request = new ConsumerIdRequest(consumerId.toString());

        return webClient
                .method(HttpMethod.DELETE)
                .uri("/")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class).block();
    }
}
