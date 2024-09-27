package br.com.fiap.postech.adjt.checkout.clients;

import br.com.fiap.postech.adjt.checkout.model.dto.request.ClearCartRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.request.FindCartByCustomerIdRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.response.CartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "cartClient", url = "${cart.service.url}/cart")
public interface CartClient {
    @GetMapping
    CartResponse consult(@RequestBody FindCartByCustomerIdRequest request);

    @DeleteMapping
    void clear(@RequestBody ClearCartRequest request);
}