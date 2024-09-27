package br.com.fiap.postech.adjt.checkout.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        template.header("apiKey", "c92cc63d352a244f31f87f09832a9abfc47aed2f0e679a3a052fd7a3164711b0");
    }
}
