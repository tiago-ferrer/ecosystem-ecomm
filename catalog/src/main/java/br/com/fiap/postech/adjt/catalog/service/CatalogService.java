
package br.com.fiap.postech.adjt.catalog.service;

import br.com.fiap.postech.adjt.catalog.model.Product;

import java.util.List;

public interface CatalogService {

    List<Product> buscaProdutos();

    Product buscaProdutoPeloID(Long id);

}