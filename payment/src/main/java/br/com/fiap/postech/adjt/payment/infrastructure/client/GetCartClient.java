package br.com.fiap.postech.adjt.payment.infrastructure.client;

import br.com.fiap.postech.adjt.payment.infrastructure.dtos.GetCartPayload;
import br.com.fiap.postech.adjt.payment.infrastructure.dtos.GetCartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "get-cart", url = "http://cart:8081/cart")
public interface GetCartClient {
    @GetMapping()
    GetCartResponse exec(GetCartPayload dto);
}
