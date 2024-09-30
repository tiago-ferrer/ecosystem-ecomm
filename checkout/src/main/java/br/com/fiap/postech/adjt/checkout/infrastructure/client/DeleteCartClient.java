package br.com.fiap.postech.adjt.checkout.infrastructure.client;

import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.GetCartPayload;
import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.GetCartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "delete-cart", url = "http://cart:8081/cart")
public interface DeleteCartClient {
    @DeleteMapping()
    GetCartResponse exec(GetCartPayload dto);
}
