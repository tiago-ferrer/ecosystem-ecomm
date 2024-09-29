package br.com.fiap.postech.adjt.checkout.clients.impl;

import br.com.fiap.postech.adjt.checkout.clients.CartClient;
import br.com.fiap.postech.adjt.checkout.model.dto.request.ClearCartRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.request.FindCartByCustomerIdRequest;
import br.com.fiap.postech.adjt.checkout.model.dto.response.CartResponse;
import br.com.fiap.postech.adjt.checkout.model.dto.response.MessageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CartClientImpl implements CartClient {

    @Value("${cart.service.url}/cart")
    private String cartUrl;

    private final RestClient restClient;

    CartClientImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public CartResponse consult(FindCartByCustomerIdRequest request) {
        ResponseEntity<CartResponse> cartConsultResponse = restClient
                .method(HttpMethod.GET)
                .uri(cartUrl)
                .body(request)
                .retrieve()
                .toEntity(CartResponse.class);

        return cartConsultResponse.getBody();
    }

    @Override
    public MessageResponse clear(ClearCartRequest request) {
        ResponseEntity<MessageResponse> cartClearResponse = restClient
                .method(HttpMethod.DELETE)
                .uri(cartUrl)
                .body(request)
                .retrieve()
                .toEntity(MessageResponse.class);

        return cartClearResponse.getBody();
    }
}
