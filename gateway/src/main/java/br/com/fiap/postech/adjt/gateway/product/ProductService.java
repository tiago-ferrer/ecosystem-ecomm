package br.com.fiap.postech.adjt.gateway.product;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ProductService {

    private final RestClient restClient = RestClient.create();

    public Product[] findAll() {
        return restClient
                .get()
                .uri("https://fakestoreapi.com/products")
                .retrieve()
                .body(Product[].class);
    }

    public Product findById(Long id) {
        ResponseEntity<Product> responseEntity = restClient
                .get()
                .uri("https://fakestoreapi.com/products/{id}", id)
                .retrieve()
                .toEntity(Product.class);
        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.hasBody()) {
            return responseEntity.getBody();
        }
        throw new ProductNotFoundException();
    }

}
