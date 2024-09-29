package br.com.fiap.postech.adjt.catalog.feign;

import br.com.fiap.postech.adjt.catalog.model.Product;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "Products", url = "https://fakestoreapi.com/products")
public interface ProductsClient {

    @Cacheable(value = "catalogCache")
    @GetMapping
    List<Product> getProducts();

    @Cacheable(value = "catalogCache")
    @GetMapping(value = "/{id}")
    Product getProductById(@PathVariable("id") Long id);

}
