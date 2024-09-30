package br.com.fiap.postech.adjt.cart.infrastructure.adapters.output.persistence.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "fakestoreapi", url = "https://fakestoreapi.com/products/")
public interface ProdutoServiceClient {
	
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id);

}
