package br.com.fiap.postech.adjt.cart.service.impl;

import br.com.fiap.postech.adjt.cart.client.ProductClient;
import br.com.fiap.postech.adjt.cart.dto.ProductDto;
import br.com.fiap.postech.adjt.cart.exceptions.CartException;
import br.com.fiap.postech.adjt.cart.test.utils.TestUtils;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductClient productClient;


    @Test
    void testFindProductByIdSuccess() {
        when(productClient.findProductById(Mockito.anyLong()))
                .thenReturn(TestUtils.buildProductDto());
        ProductDto productDto = productService.findProductById(1L);
        assertNotNull(productDto);
        assertEquals(1L, productDto.getId());
    }

    @Test
    void testFindProductByIdNotFound() {
        when(productClient.findProductById(Mockito.anyLong()))
                .thenThrow(FeignException.NotFound.class);
        String msgError = assertThrows(CartException.class, () ->
                productService.findProductById(1L)).getMessage();
        assertEquals("Invalid itemId does not exist", msgError);
    }

}
