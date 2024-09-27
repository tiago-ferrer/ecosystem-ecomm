package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.dto.ConsumerIdRequest;
import br.com.fiap.postech.adjt.checkout.model.Cart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CartServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CartService cartService;

    private String cartServiceUrl = "http://localhost:8081";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartService = new CartService(restTemplate, cartServiceUrl);
    }

    @Test
    void getCartShouldReturnCart() {
        UUID consumerId = UUID.randomUUID();
        Cart expectedCart = Cart.builder().build();

        when(restTemplate.postForObject(eq(cartServiceUrl), any(ConsumerIdRequest.class), eq(Cart.class)))
                .thenReturn(expectedCart);

        Cart actualCart = cartService.getCart(consumerId);

        assertNotNull(actualCart);
        verify(restTemplate).postForObject(cartServiceUrl, new ConsumerIdRequest(consumerId.toString()), Cart.class);
    }

    @Test
    void clearCartShouldCallDeleteEndpoint() {
        UUID consumerId = UUID.randomUUID();
        ConsumerIdRequest request = new ConsumerIdRequest(consumerId.toString());

        cartService.clearCart(consumerId);

        verify(restTemplate).exchange(cartServiceUrl, HttpMethod.DELETE, new HttpEntity<>(request), Void.class);
    }
}
