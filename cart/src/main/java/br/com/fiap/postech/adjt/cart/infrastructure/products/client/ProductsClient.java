package br.com.fiap.postech.adjt.cart.infrastructure.products.client;

import br.com.fiap.postech.adjt.cart.infrastructure.products.client.response.ProductsResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "products", url = "http://products:8083")
public interface ProductsClient {

    @GetMapping(value = "/item/{itemId}/{quantity}")
    ProductsResponseDTO busca(@PathVariable(value = "itemId") final Long itemId,
                              @PathVariable(value = "quantity") final Long quantity);

}
