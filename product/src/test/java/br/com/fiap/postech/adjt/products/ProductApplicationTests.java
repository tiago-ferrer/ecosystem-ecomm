package br.com.fiap.postech.adjt.products;

import br.com.fiap.postech.adjt.products.client.ProductClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ProductApplicationTests {

	@Autowired
	private ProductClient productClient;

	@Test
	void contextLoads() {
		assertNotNull(productClient);
	}

}
