package br.com.fiap.postech.adjt.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                .route("cart", r -> r.path("/cart/**").uri(CART_URL))
                .route("checkout", r -> r.path("/checkout/**").uri(CHECKOUT_URL))
                .route("product", r -> r.path("/products/**").uri(PRODUCTS_URL))
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
