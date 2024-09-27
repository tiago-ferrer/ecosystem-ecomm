package br.com.fiap.postech.adjt.products.client;


import br.com.fiap.postech.adjt.products.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "product", url = "${URL_PRODUCT}")
public interface ProductClient {

    @GetMapping("/products")
    List<ProductDto>findAllProducts();

    @GetMapping("/products/{id}")
    ProductDto findProductById(@PathVariable("id") Long id);

}
