package br.com.fiap.postech.adjt.checkout.infrastructure.config;

import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientFastConfig {

    @Bean
    public Request.Options options() {
        return new Request.Options(300, 300);
    }

}
