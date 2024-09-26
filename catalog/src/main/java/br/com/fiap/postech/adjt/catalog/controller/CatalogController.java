package br.com.fiap.postech.adjt.catalog.controller;

import br.com.fiap.postech.adjt.catalog.model.Product;
import br.com.fiap.postech.adjt.catalog.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping
    public List<Product> buscaProdutos() {
        return catalogService.buscaProdutos();
    }

    @GetMapping("/{id}")
    public Product buscaProdutoPeloID(@PathVariable Long id) {
        return catalogService.buscaProdutoPeloID(id);
    }

}