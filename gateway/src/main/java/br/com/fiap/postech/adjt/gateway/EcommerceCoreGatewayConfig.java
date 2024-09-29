package br.com.fiap.postech.adjt.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.method;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class EcommerceCoreGatewayConfig {

    @Value("${URL_CART}")
    private String urlCart;

    @Value("${URL_CHECKOUT}")
    private String urlCheckout;

    @Value("${URL_PRODUCT}")
    private String urlProduct;


    @Bean
    public RouterFunction<ServerResponse> gatewayRouterFunctionsAddReqHeader() {
        return route()
                .route(method(HttpMethod.GET).and(path("/products/**")), http(urlProduct))
                .route(method(HttpMethod.GET).and(path("/cart")), http(urlCart))
                .route(method(HttpMethod.POST).and(path("/cart/items")), http(urlCart))
                .route(method(HttpMethod.PUT).and(path("/cart/item")), http(urlCart))
                .route(method(HttpMethod.DELETE).and(path("/cart/item")), http(urlCart))
                .route(method(HttpMethod.POST).and(path("/checkout/**")), http(urlCheckout))
                .route(method(HttpMethod.GET).and(path("/checkout/**")), http(urlCheckout))
                .route(method(HttpMethod.GET).and(path("/by-order-id/**")), http(urlCheckout))
                .route(method(HttpMethod.POST).and(path("/by-order-id/**")), http(urlCheckout))
                .build();
    }


}
