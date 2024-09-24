package br.com.fiap.postech.adjt.catalog.feign;

import br.com.fiap.postech.adjt.catalog.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "Products", url = "https://fakestoreapi.com/products")
public interface ProductsClient {

    @GetMapping
    List<Product> getProducts();

    @GetMapping(value = "/{id}")
    Product getProductById(@PathVariable("id") Long id);

    @PostMapping
    Product createProducts(@RequestBody Product product);

    @PutMapping
    Product updateProducts(@RequestBody Product product);

    @DeleteMapping(value = "/{id}")
    Product deleteProductById(@PathVariable("id") Long id);

}
