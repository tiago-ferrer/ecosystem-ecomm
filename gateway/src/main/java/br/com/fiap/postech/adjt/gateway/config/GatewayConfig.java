package br.com.fiap.postech.adjt.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Configuration
public class GatewayConfig {
    private final WebClient webClient;

    public GatewayConfig(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("products_route", r -> r.path("/products/**")
                        .filters(f -> f.filter((exchange, chain) -> {
                            return callExternalApiWithTimeout("https://fakestoreapi.com" + exchange.getRequest().getURI().getPath())
                                    .flatMap(response -> {
                                        exchange.getResponse().setStatusCode(response.getStatusCode());
                                        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(response.getBody().getBytes())));
                                    })
                                    .onErrorResume(throwable -> {
                                        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                                        return exchange.getResponse().setComplete();
                                    });
                        }))
                        .uri("https://fakestoreapi.com")
                )
                .build();
    }

    private Mono<ResponseEntity<String>> callExternalApiWithTimeout(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .toEntity(String.class)
                .timeout(Duration.ofMillis(500));
    }
}
