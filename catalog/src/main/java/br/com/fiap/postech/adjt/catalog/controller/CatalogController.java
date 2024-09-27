package br.com.fiap.postech.adjt.catalog.controller;

import br.com.fiap.postech.adjt.catalog.model.Product;
import br.com.fiap.postech.adjt.catalog.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    @Cacheable("exampleCache")
    @GetMapping
    public List<Product> buscaProdutos() {
        return catalogService.buscaProdutos();
    }

    @Cacheable("exampleCache")
    @GetMapping("/{id}")
    public Product buscaProdutoPeloID(@PathVariable Long id) {
        return catalogService.buscaProdutoPeloID(id);
    }

}