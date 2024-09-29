package br.com.fiap.postech.adjt.catalog.controller;

import br.com.fiap.postech.adjt.catalog.model.Product;
import br.com.fiap.postech.adjt.catalog.model.Rating;
import br.com.fiap.postech.adjt.catalog.service.CatalogService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class CatalogControllerTest {

    private CatalogController catalogController;
    @Mock
    private CatalogService catalogService;
    private Product product;
    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        catalogController = new CatalogController(catalogService);
        product = Product.builder()
                .id(1L)
                .title("produto 1")
                .price(new BigDecimal(100.00))
                .description("descricao do produto 1")
                .category("categoria 1")
                .image("produto1.jpg")
                .rating(new Rating(new BigDecimal(10.0), 1))
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void devebuscaProdutosComSucesso() {
        var listProdutos = new ArrayList<Product>();
        listProdutos.add(product);
        when(catalogService.buscaProdutos()).thenReturn(listProdutos);

        var response = catalogController.buscaProdutos();

        verify(catalogService, times(1)).buscaProdutos();
        Assertions.assertEquals(product.getId(), response.get(0).getId());
        Assertions.assertEquals(product.getTitle(), response.get(0).getTitle());
        Assertions.assertEquals(product.getPrice(), response.get(0).getPrice());
        Assertions.assertEquals(product.getDescription(), response.get(0).getDescription());
        Assertions.assertEquals(product.getCategory(), response.get(0).getCategory());
        Assertions.assertEquals(product.getImage(), response.get(0).getImage());
        Assertions.assertEquals(product.getRating().getRate(), response.get(0).getRating().getRate());
        Assertions.assertEquals(product.getRating().getCount(), response.get(0).getRating().getCount());
    }

    @Test
    void devebuscaProdutoPeloIDComSucesso() {
        when(catalogService.buscaProdutoPeloID(product.getId())).thenReturn(product);

        var response = catalogController.buscaProdutoPeloID(product.getId());

        verify(catalogService, times(1)).buscaProdutoPeloID(product.getId());
        Assertions.assertEquals(product.getId(), response.getId());
        Assertions.assertEquals(product.getTitle(), response.getTitle());
        Assertions.assertEquals(product.getPrice(), response.getPrice());
        Assertions.assertEquals(product.getDescription(), response.getDescription());
        Assertions.assertEquals(product.getCategory(), response.getCategory());
        Assertions.assertEquals(product.getImage(), response.getImage());
        Assertions.assertEquals(product.getRating().getRate(), response.getRating().getRate());
        Assertions.assertEquals(product.getRating().getCount(), response.getRating().getCount());
    }

}
