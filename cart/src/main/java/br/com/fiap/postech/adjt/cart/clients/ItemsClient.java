package br.com.fiap.postech.adjt.cart.clients;

import br.com.fiap.postech.adjt.cart.model.dto.response.ItemResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "items", url = "${items.url}/products")
public interface ItemsClient {

    @GetMapping(value = "/{id}")
    ResponseEntity<ItemResponse> findById(@PathVariable Long id);
}
