package br.com.fiap.postech.adjt.products.infrastructure.fakeStoreApi.client;

import br.com.fiap.postech.adjt.products.infrastructure.fakeStoreApi.client.response.ItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "item", url = "https://fakestoreapi.com/products")
public interface FakeStoreApiClient {

    @GetMapping(value = "/{itemId}")
    ItemDTO pegaItem(@PathVariable(value = "itemId") final Long itemId);

}
