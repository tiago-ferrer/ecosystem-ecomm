package br.com.fiap.postech.adjt.gateway.controller.product;

import br.com.fiap.postech.adjt.gateway.domain.product.Product;
import br.com.fiap.postech.adjt.gateway.infra.exception.ProductNotFoundException;
import br.com.fiap.postech.adjt.gateway.domain.product.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        Product[] products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            Product product = productService.findById(id);
            return ResponseEntity.ok(product);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

}
