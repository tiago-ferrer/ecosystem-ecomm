package br.com.fiap.postech.adjt.catalog.service;

import br.com.fiap.postech.adjt.catalog.feign.ProductsClient;
import br.com.fiap.postech.adjt.catalog.model.Product;
import br.com.fiap.postech.adjt.catalog.model.Rating;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class CatalogServiceImplTest {

    private CatalogServiceImpl catalogService;
    @Mock
    private ProductsClient productsClient;
    private Product product;
    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        catalogService = new CatalogServiceImpl(productsClient);
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
        when(productsClient.getProducts()).thenReturn(listProdutos);

        var response = catalogService.buscaProdutos();

        verify(productsClient, times(1)).getProducts();
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
    void devebuscaProdutosComErro() {
        when(productsClient.getProducts()).thenThrow(RuntimeException.class);
        Assertions.assertThrows(RuntimeException.class, () -> catalogService.buscaProdutos());
    }

    @Test
    void devebuscaProdutoPeloIDComSucesso() {
        when(productsClient.getProductById(product.getId())).thenReturn(product);

        var response = catalogService.buscaProdutoPeloID(product.getId());

        verify(productsClient, never()).getProducts();
        Assertions.assertEquals(product.getId(), response.getId());
        Assertions.assertEquals(product.getTitle(), response.getTitle());
        Assertions.assertEquals(product.getPrice(), response.getPrice());
        Assertions.assertEquals(product.getDescription(), response.getDescription());
        Assertions.assertEquals(product.getCategory(), response.getCategory());
        Assertions.assertEquals(product.getImage(), response.getImage());
        Assertions.assertEquals(product.getRating().getRate(), response.getRating().getRate());
        Assertions.assertEquals(product.getRating().getCount(), response.getRating().getCount());
    }

    @Test
    void devebuscaProdutoPeloIDComErro() {
        when(productsClient.getProductById(product.getId())).thenThrow(RuntimeException.class);
        Assertions.assertThrows(RuntimeException.class, () -> catalogService.buscaProdutoPeloID(product.getId()));
    }

}
