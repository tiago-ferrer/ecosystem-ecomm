package br.com.fiap.postech.adjt.checkout.dataprovider.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClientPayment(WebClient.Builder builder) {
        return builder
                .baseUrl("https://payment-api-latest.onrender.com")//TODO mandar para variavel de ambiente
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("apiKey", "colocar-key-grup")//TODO colocar key
                .build();
    }

    @Bean
    public WebClient webClientCart(WebClient.Builder builder) {
        return builder
                .baseUrl("http://cart:8081")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
