package br.com.fiap.postech.adjt.products.controller;

import br.com.fiap.postech.adjt.products.dto.ProductDto;
import br.com.fiap.postech.adjt.products.service.ProductService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> findAllProducts() {
        return productService.findAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findProductById(@PathVariable Long id) {
        return Optional.ofNullable(productService.findAllProducts())
                .map(ResponseEntity::getBody)
                .filter(products -> !products.isEmpty())
                .flatMap(products -> products.stream().filter(product -> product.getId().equals(id)).findFirst())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


}
