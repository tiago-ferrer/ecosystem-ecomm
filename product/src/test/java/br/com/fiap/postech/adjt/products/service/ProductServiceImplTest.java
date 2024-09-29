package br.com.fiap.postech.adjt.products.service;

import br.com.fiap.postech.adjt.products.client.ProductClient;
import br.com.fiap.postech.adjt.products.dto.ProductDto;
import br.com.fiap.postech.adjt.products.service.impl.ProductServiceImpl;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductClient productClient;

    @Test
    void testFindAllProducts() {
        when(productClient.findAllProducts()).thenReturn(List.of(TestUtils.buildProductDto()));
        ResponseEntity<List<ProductDto>> products = productService.findAllProducts();
        assertNotNull(products);
        assertEquals(HttpStatus.OK, products.getStatusCode());
        assertNotNull(products.getBody());
        assertEquals(1, products.getBody().size());
    }

    @Test
    void testFindAllProductsNotFound() {
        when(productClient.findAllProducts()).thenThrow(FeignException.NotFound.class);
        assertThrows(FeignException.NotFound.class, () -> productService.findAllProducts());
    }

    @Test
    void testFindProductById() {
        when(productClient.findProductById(Mockito.anyLong())).thenReturn(TestUtils.buildProductDto());
        ResponseEntity<ProductDto> product = productService.findProductById(1L);
        assertNotNull(product);
        assertEquals(HttpStatus.OK, product.getStatusCode());
        assertNotNull(product.getBody());
        assertEquals(1, product.getBody().getId());
    }

    @Test
    void testFindProductByIdWhenNotFound() {
        when(productClient.findProductById(Mockito.anyLong())).thenReturn(null);
        ResponseEntity<ProductDto> product = productService.findProductById(1L);
        assertNotNull(product);
        assertEquals(HttpStatus.NOT_FOUND, product.getStatusCode());
        assertNull(product.getBody());
    }

}
