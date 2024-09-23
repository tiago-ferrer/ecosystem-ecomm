package br.com.fiap.postech.adjt.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.web.servlet.function.RequestPredicates.GET;
import static org.springframework.web.servlet.function.RequestPredicates.POST;

public class GatewayConfig {

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions
                .route(POST("/cart/**").or(GET("/cart/**")), request ->
                        ServerResponse.permanentRedirect(URI.create("http://localhost" + request.uri().getPath())).build()
                ).andRoute(POST("/cart/**").negate().and(GET("/cart/**").negate()), request ->
                    ServerResponse.status(404).body("Method " + request.method() + "Not allowed")
                );
    }
}
