package br.com.fiap.postech.adjt.cart.feign;
import br.com.fiap.postech.adjt.cart.dto.ProductDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "product-client", url = "https://fakestoreapi.com")
public interface ProductClient {
    @GetMapping("/products/{itemId}")
    ProductDto getProductById(@PathVariable("itemId") Long itemId);
}