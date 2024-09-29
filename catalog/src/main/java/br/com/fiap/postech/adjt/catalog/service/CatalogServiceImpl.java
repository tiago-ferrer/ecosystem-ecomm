package br.com.fiap.postech.adjt.catalog.service;

import br.com.fiap.postech.adjt.catalog.feign.ProductsClient;
import br.com.fiap.postech.adjt.catalog.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final ProductsClient productClient;

    public List<Product> buscaProdutos() {
        try {
            return productClient.getProducts();
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um problema na busca dos produtos. Exceção: ", e);
        }
    }

    public Product buscaProdutoPeloID(Long id) {
        try {
            return productClient.getProductById(id);
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um problema na busca do produto codigo " + id + ". Exceção: ", e);
        }
    }

}