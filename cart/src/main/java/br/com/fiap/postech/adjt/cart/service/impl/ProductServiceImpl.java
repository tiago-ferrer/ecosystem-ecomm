package br.com.fiap.postech.adjt.cart.service.impl;

import br.com.fiap.postech.adjt.cart.client.ProductClient;
import br.com.fiap.postech.adjt.cart.dto.ProductDto;
import br.com.fiap.postech.adjt.cart.exceptions.CartException;
import br.com.fiap.postech.adjt.cart.service.ProductService;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductClient productClient;

    @Override
    public ProductDto findProductById(Long id) {
        try {
            return productClient.findProductById(id);
        } catch (FeignException.NotFound e) {
            throw new CartException("Invalid itemId does not exist");
        }
    }


}
