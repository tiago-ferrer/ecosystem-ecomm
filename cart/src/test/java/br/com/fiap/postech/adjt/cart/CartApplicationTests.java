package br.com.fiap.postech.adjt.cart;

import br.com.fiap.postech.adjt.cart.service.CartService;
import br.com.fiap.postech.adjt.cart.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase
class CartApplicationTests {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Test
    void testContextLoads() {

        assertNotNull(cartService);
        assertNotNull(productService);
        CartApplication.main(new String[]{});

    }

}
