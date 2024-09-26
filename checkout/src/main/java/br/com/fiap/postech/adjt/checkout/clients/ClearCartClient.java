package br.com.fiap.postech.adjt.checkout.clients;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "clearCartClient", url = "${cart.service.url}")
public interface ClearCartClient {
    @DeleteMapping("/cart")
    void clearCart(@RequestParam("consumerId") UUID consumerId);
}