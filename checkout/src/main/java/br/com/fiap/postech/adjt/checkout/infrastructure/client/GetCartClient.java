package br.com.fiap.postech.adjt.checkout.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.GetCartPayload;
import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.GetCartResponse;

@FeignClient(value = "get-cart", url = "http://cart:8081/cart")
public interface GetCartClient {
    @GetMapping()
    GetCartResponse exec(GetCartPayload dto);
}
