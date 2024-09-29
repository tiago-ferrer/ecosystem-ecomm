package br.com.fiap.postech.adjt.products.client;

import br.com.fiap.postech.adjt.products.dto.ProductDto;
import br.com.fiap.postech.adjt.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class FindProducts {

    private final ProductService productService;

    @Scheduled(fixedRate = 600000)
    @Caching(cacheable = {@Cacheable(value = "products")}, put = {@CachePut(value = "products")})
    public ResponseEntity<List<ProductDto>> findAllProductsScheduled() {
        return productService.findAllProducts();
    }


}
