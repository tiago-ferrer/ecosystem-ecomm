package br.com.fiap.postech.adjt.cart.client;

import br.com.fiap.postech.adjt.cart.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product", url = "${URL_PRODUCT}")
public interface ProductClient {

    @GetMapping("/products/{id}")
    ProductDto findProductById(@PathVariable("id") Long id);

}
