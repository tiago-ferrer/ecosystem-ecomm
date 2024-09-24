package br.com.fiap.postech.adjt.checkout.clients;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.fiap.postech.adjt.checkout.model.response.CartResponse;

@FeignClient(name = "cartClient", url = "${cart.service.url}")
public interface CartClient {
    @GetMapping (value = "/cart", produces = MediaType.APPLICATION_JSON_VALUE)
     List<CartResponse> consultCart(@RequestParam("consumerId") UUID consumerId);
}
