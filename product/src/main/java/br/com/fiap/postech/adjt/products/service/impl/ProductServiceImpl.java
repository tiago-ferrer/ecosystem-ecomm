package br.com.fiap.postech.adjt.products.service.impl;

import br.com.fiap.postech.adjt.products.client.ProductClient;
import br.com.fiap.postech.adjt.products.dto.ProductDto;
import br.com.fiap.postech.adjt.products.service.ProductService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductClient productClient;


    @Override
    @Cacheable({"products"})
    public ResponseEntity<List<ProductDto>> findAllProducts() {
        log.info("chamando catalogo de produtos...");
        return ResponseEntity.ok(productClient.findAllProducts());
    }

    @Override
    public ResponseEntity<ProductDto> findProductById(Long id) {
        log.info("chamando catalogo de produtos por id {}...", id);
        return Optional.ofNullable(productClient.findProductById(id))
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


}