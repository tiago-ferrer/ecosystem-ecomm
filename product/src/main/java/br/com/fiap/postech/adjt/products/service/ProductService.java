package br.com.fiap.postech.adjt.products.service;

import br.com.fiap.postech.adjt.products.dto.ProductDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    ResponseEntity<List<ProductDto>> findAllProducts();
    ResponseEntity<ProductDto> findProductById(Long id);

}
