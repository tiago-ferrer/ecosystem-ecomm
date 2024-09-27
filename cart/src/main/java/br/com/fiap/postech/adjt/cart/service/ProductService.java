package br.com.fiap.postech.adjt.cart.service;

import br.com.fiap.postech.adjt.cart.dto.ProductDto;
import org.springframework.http.ResponseEntity;

public interface ProductService {

    ProductDto findProductById(Long id);

}
