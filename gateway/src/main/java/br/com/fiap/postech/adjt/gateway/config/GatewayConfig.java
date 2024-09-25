package br.com.fiap.postech.adjt.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()
                // Rotas para o serviço de carrinho
                .route("Get cart", r -> r.path("/cart/**")
                        .and().method("GET", "POST")
                        .uri("http://localhost:8080"))

                .route("Method Not Allowed", r -> r.path("/cart/**")
                        .and().method("PUT", "DELETE")
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
