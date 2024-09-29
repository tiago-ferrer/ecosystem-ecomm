package br.com.fiap.postech.adjt.checkout.consumer;

import br.com.fiap.postech.adjt.checkout.dto.cart.CartDto;
import br.com.fiap.postech.adjt.checkout.dto.cart.CartRequest;
import br.com.fiap.postech.adjt.checkout.dto.cart.CartResponseRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name= "cart", url ="${URL_CART}")
public interface CartClient {
    @GetMapping("/cart")
     ResponseEntity<CartDto> findByConsumerId(@RequestBody CartRequest cartRequest);

    @DeleteMapping
    ResponseEntity<CartResponseRecord> deleteByConsumerId(@RequestBody CartRequest cartRequest);

}
