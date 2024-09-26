package br.com.fiap.postech.adjt.cart.feign;

import br.com.fiap.postech.adjt.cart.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductClientTest {

    @MockBean
    private ProductClient productClient;

    @Test
    public void testGetProductById() {
        Long itemId = 1L;
        ProductDto mockProduct = new ProductDto(itemId, "Test Product", new BigDecimal("20.00"), "Description", "Category", "ImageUrl");

        when(productClient.getProductById(itemId)).thenReturn(mockProduct);

        ProductDto result = productClient.getProductById(itemId);

        assertEquals(mockProduct, result);
    }
}