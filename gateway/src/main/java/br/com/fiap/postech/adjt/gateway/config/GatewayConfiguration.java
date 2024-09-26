package br.com.fiap.postech.adjt.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfiguration {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/cart/**")
                        .uri("lb://cart"))
                .route(r -> r.path("/checkout/**")
                        .uri("lb://checkout"))
                .route(r -> r.path("/products/**")
                        .uri("lb://products"))
                .build();
    }
}
