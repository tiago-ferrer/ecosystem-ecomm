package br.com.fiap.postech.adjt.products.controller;

import br.com.fiap.postech.adjt.products.dto.ProductDto;
import br.com.fiap.postech.adjt.products.service.ProductService;
import br.com.fiap.postech.adjt.products.service.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void close() throws Exception {
        mocks.close();
    }

    @Test
    void testFindAllProducts() {
        List<ProductDto> productList = new ArrayList<>();
        when(productService.findAllProducts()).thenReturn(ResponseEntity.ok(productList));
        ResponseEntity<List<ProductDto>> response = productController.findAllProducts();
        assertEquals(ResponseEntity.ok(productList), response);
    }

    @Test
    void testFindProductById() {
        when(productService.findAllProducts()).thenReturn(ResponseEntity.ok(List.of(TestUtils.buildProductDto())));
        ResponseEntity<ProductDto> response = productController.findProductById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
