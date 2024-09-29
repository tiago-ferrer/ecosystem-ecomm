package br.com.fiap.postech.adjt.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        AtomicReference<String> uriProducts = new AtomicReference<>("https://fakestoreapi.com");

        return builder.routes()
                // Rotas para o serviço de carrinho
                .route("ms-cart", r -> r.path("/cart/**")
                        .and().method("GET", "POST", "PUT")
                        .uri("http://cart:8081"))

                .route("Method Not Allowed", r -> r.path("/cart/**")
                        .and().method("DELETE")
                        .filters(f -> f.setStatus(HttpStatus.NOT_FOUND)
                                .modifyResponseBody(String.class, String.class, (exchange, body) -> {
                                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                                    return Mono.just("{\"message\": \"Método não permitido\"}");
                                })
                        )
                        .uri("http://cart:8081")
                )
                .route("ms-checkout", r -> r.path("/checkout/**")
                        .and().method("GET", "POST")
                        .uri("http://checkout:8082"))

                .route("products-external", r -> r.path("/products/**")
                        .and().method("GET")
                        .filters(f -> f
                                .modifyResponseBody(String.class, String.class, (serverWebExchange, body) -> {
                                    if (serverWebExchange.getResponse().getStatusCode() == HttpStatus.OK && (body == null || body.isEmpty())) {
                                        serverWebExchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                                        serverWebExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                                        uriProducts.set("http://localhost:8080");
                                        return Mono.just("{\"message\": \"Id não localizado\"}");
                                    }
                                    return Mono.just(body);
                                })
                        ).uri(uriProducts.get()))

                .route("Method Not Allowed", r -> r.path("/products/**")
                        .and().method("POST", "DELETE", "PUT", "PATCH")
                        .filters(f -> f.setStatus(HttpStatus.NOT_FOUND)
                                .modifyResponseBody(String.class, String.class, (exchange, body) -> {
                                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                                    return Mono.just("{\"message\": \"Método não permitido\"}");
                                })
                        )
                        .uri("http://localhost:8080")
                )
                .build();
    }
}
