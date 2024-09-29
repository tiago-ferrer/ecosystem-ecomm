package br.com.fiap.postech.adjt.checkout.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${URL_PAYMENT}")
    private String urlPaymentApi;

    @Bean(name = "webClientPayment")
    public WebClient webClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl(urlPaymentApi).build();
    }


}
