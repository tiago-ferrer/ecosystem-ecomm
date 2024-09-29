package br.com.fiap.postech.adjt.gateway.product;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.util.ReflectionTestUtils.setField;

public class ProductServiceTest {

    private ProductService productService;

    AutoCloseable mock;

    @BeforeEach
    void setup() {
        this.mock = MockitoAnnotations.openMocks(this);
        this.productService = new ProductService();
        setField(this.productService, "productURI", "https://fakestoreapi.com/products");
    }

    @AfterEach
    void tearDown() throws Exception {
        this.mock.close();
    }

    @Test
    void shouldFindAll() {
        // Arrange


        // Act
        Product[] products = this.productService.findAll();

        // Assert
        assertThat(products).hasSize(20);
    }

    @Nested
    class FindById {

        @Test
        void shouldFindById() {
            // Arrange
            Long id = 1L;

            // Act
            Product product = productService.findById(id);

            // Assert
            assertThat(product)
                    .isNotNull()
                    .isInstanceOf(Product.class);
        }

        @Test
        void shouldThrowProductNotFoundException() {
            // Arrange
            Long id = 123L;

            // Act & Assert
            assertThatThrownBy(() -> productService.findById(id))
                    .isInstanceOf(ProductNotFoundException.class);
        }

    }

}
