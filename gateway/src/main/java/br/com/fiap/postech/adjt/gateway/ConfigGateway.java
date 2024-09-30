package br.com.fiap.postech.adjt.gateway;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
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
                        .uri("http://checkout:8082"))
                .route("products", r -> r.path("/products/**")
                        .filters(f -> f.modifyResponseBody(String.class, String.class,
                                (exchange, s) -> {
                               if (s == null) {
                                 exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                                 return Mono.just("Id não localizado");
                               };
                              return Mono.just(s);
                                })).uri("https://fakestoreapi.com"))
                .build();
    }
}
