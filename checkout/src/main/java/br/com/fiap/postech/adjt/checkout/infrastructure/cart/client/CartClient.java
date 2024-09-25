package br.com.fiap.postech.adjt.checkout.infrastructure.cart.client;

import br.com.fiap.postech.adjt.checkout.infrastructure.cart.client.request.CartRequestDTO;
import br.com.fiap.postech.adjt.checkout.infrastructure.cart.client.response.CartResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "cart", url = "http://localhost:8081")
public interface CartClient {

    @DeleteMapping
    CartResponseDTO deletaOCarrinho(@RequestBody final CartRequestDTO request);

}
