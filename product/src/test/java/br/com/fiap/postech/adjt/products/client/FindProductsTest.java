package br.com.fiap.postech.adjt.products.client;

import br.com.fiap.postech.adjt.products.dto.ProductDto;
import br.com.fiap.postech.adjt.products.service.ProductService;
import br.com.fiap.postech.adjt.products.service.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindProductsTest {

    @InjectMocks
    private FindProducts findProducts;

    @Mock
    private ProductService productService;

    @Test
    void testFindAllProductsScheduled() {
        when(productService.findAllProducts())
                .thenReturn(ResponseEntity.ok(List.of(TestUtils.buildProductDto())));
        ResponseEntity<List<ProductDto>> products = findProducts.findAllProductsScheduled();
        assertEquals(HttpStatus.OK, products.getStatusCode());
    }


}
