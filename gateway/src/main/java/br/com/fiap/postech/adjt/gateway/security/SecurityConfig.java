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
<<<<<<< HEAD
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/cart/**").permitAll()
                        .pathMatchers("/checkout/**").authenticated()
                        .pathMatchers("/products/**").permitAll()
                        .anyExchange().authenticated()
=======
                .csrf(csrf -> csrf.disable()) // Desabilitar CSRF
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/cart/**").permitAll() // Permitir acesso ao carrinho
                        .pathMatchers("/checkout/**").authenticated() // Proteger checkout
                        .pathMatchers("/products/**").permitAll() // Permitir acesso aos produtos
                        .anyExchange().authenticated() // Qualquer outra requisição deve ser autenticada
>>>>>>> 79b1335a998168ed3b4359d3ce24c4ea73b61784
                );

        return http.build();
    }
}
