package br.com.fiap.postech.adjt.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable()) // Desabilitar CSRF
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/cart/**").permitAll() // Permitir acesso ao carrinho
                        .pathMatchers("/checkout/**").authenticated() // Proteger checkout
                        .pathMatchers("/products/**").permitAll() // Permitir acesso aos produtos
                        .anyExchange().authenticated() // Qualquer outra requisição deve ser autenticada
                );

        return http.build();
    }
}
