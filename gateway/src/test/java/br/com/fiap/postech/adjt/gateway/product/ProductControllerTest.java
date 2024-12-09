package br.com.fiap.postech.adjt.gateway.product;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    AutoCloseable mock;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
        ProductController productController = new ProductController(productService);
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }, "/*")
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void findAll() throws Exception {
        // Act & Assert
        mockMvc.perform(
                        get("/products"))
                .andExpect(status().is2xxSuccessful());
    }

    @Nested
    class FindById {

        @Test
        void shouldFindById() throws Exception {
            // Act & Assert
            mockMvc.perform(
                            get("/products/1"))
                    .andExpect(status().is2xxSuccessful());
        }

        @Test
        void shouldThrowProductNotFoundException() throws Exception {
            // Assert
            when(productService.findById(any(Long.class))).thenThrow(new ProductNotFoundException());

            // Act
            mockMvc.perform(
                            get("/products/123"))
                    .andExpect(status().is4xxClientError());
        }

    }



}
