package br.com.fiap.postech.adjt.gateway;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class ConfigGateway {
    @Bean
    public RouteLocator custom(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("cart", r -> r.path("/cart/**")
                        .and()
                        .not( p -> p.path("/cart/api/**"))
                        .filters(f -> f)
                        .uri("http://cart:8081"))
                .route("checkout", r -> r.path("/checkout/**")
                        .and()
                        .not( p -> p.path("/checkout/api/**"))
                        .filters(f -> f)
                        .uri("http://localhost:8082"))
                .route("products", r -> r.path("/products/**")
                        .filters(f -> f.modifyResponseBody(String.class, String.class,
                                (exchange, s) -> Mono.just(s.toUpperCase()))).uri("https://fakestoreapi.com"))
                .build();
    }
}
