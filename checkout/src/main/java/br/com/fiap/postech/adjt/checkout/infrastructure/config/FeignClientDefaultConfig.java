package br.com.fiap.postech.adjt.checkout.infrastructure.config;

import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientDefaultConfig {

    @Bean
    public Request.Options optionsAsync() {
        return new Request.Options(10000, 10000);
    }

}
