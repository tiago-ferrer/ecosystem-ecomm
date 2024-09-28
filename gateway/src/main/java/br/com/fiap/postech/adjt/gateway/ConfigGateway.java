package br.com.fiap.postech.adjt.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ConfigGateway {

    @Value("${api.cart.url}")
    private String CART_URL;

    @Value("${api.checkout.url}")
    private String CHECKOUT_URL;

    @Value("${api.products.url}")
    private String PRODUCTS_URL;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("cart", r -> r.path("/cart/item/**")
                        .uri(CART_URL))
                .route("cart", r -> r.path("/cart/**")
                        .and()
                        .not(p -> p.method(HttpMethod.DELETE))
                        .uri(CART_URL))
                .route("checkout", r -> r.path("/checkout/**")
                        .and()
                        .not(p -> p.method(HttpMethod.DELETE))
                        .uri(CHECKOUT_URL))
                .route("product", r -> r.path("/products/**")
                        .and()
                        .not(p -> p.method(HttpMethod.DELETE))
                        .uri(PRODUCTS_URL))
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
