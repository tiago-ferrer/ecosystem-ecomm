package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.dto.CartDTO;
import br.com.fiap.postech.adjt.checkout.dto.CartItemDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
public class CartService {
    private final RestTemplate restTemplate;

    @Value("${cart.api.url.getCart}")
    private String getCartUrl;

    public CartService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CartDTO getCart(UUID consumerId) {
        String url = getCartUrl + "?consumerId=" + consumerId;
        System.out.println("URL "+url);

        ResponseEntity<List<CartItemDTO>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<List<CartItemDTO>>() {});
        System.out.println("response "+response);
        System.out.println("Response Body: " + response.getBody());

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("response.getStatusCode().is2xxSuccessful() "+response.getStatusCode().is2xxSuccessful());
            List<CartItemDTO> items = response.getBody();
            System.out.println("items "+items);
            System.out.println("consumerId "+consumerId);
            return new CartDTO(consumerId, items);
        } else {
            System.out.println("ERROR ELSE "+response.getStatusCode());
            throw new RuntimeException("Erro ao obter o carrinho: " + response.getStatusCode());
        }
    }

    public boolean isCartEmpty(UUID consumerId) {
        CartDTO cart = getCart(consumerId);

        return cart == null || cart.items().isEmpty();
    }

    public void clearCart(UUID consumerId) {
        String url = getCartUrl + "/clear?consumerId=" + consumerId;

        ResponseEntity<Void> response = restTemplate.exchange(
                url, HttpMethod.DELETE, null, Void.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Erro ao limpar o carrinho: " + response.getStatusCode());
        }
    }
}
