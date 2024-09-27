package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.model.Cart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @MockBean
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private CartService cartService = new CartService(webClientBuilder);

    private String baseUrl = "http://localhost:8080";

    private UUID consumerId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mockando a construção do WebClient
        when(webClientBuilder.baseUrl(baseUrl)).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        this.cartService = new CartService(webClientBuilder);  // Injeção manual se necessário

        consumerId = UUID.randomUUID();
    }

    @Test
    void getCart_ShouldReturnCart() {
        Cart mockCart = new Cart(consumerId, null);

        // Mocking WebClient behavior
        when(webClient.method(eq(HttpMethod.GET))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(eq("/"))).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Cart.class)).thenReturn(Mono.just(mockCart));

        // Act
        Mono<Cart> result = cartService.getCart(consumerId);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(cart -> cart.getConsumerId().equals(consumerId))
                .verifyComplete();

        // Verify interactions
        verify(webClient).method(eq(HttpMethod.GET));
        verify(requestBodyUriSpec).uri(eq("/"));
        verify(requestBodySpec).bodyValue(any());
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).bodyToMono(Cart.class);
    }

    @Test
    void clearCart_ShouldClearCart() {
        // Mocking WebClient behavior for DELETE request
        when(webClient.method(eq(HttpMethod.DELETE))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(eq("/"))).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        // Act
        cartService.clearCart(consumerId);

        // Verify interactions
        verify(webClient).method(eq(HttpMethod.DELETE));
        verify(requestBodyUriSpec).uri(eq("/"));
        verify(requestBodySpec).bodyValue(any());
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).bodyToMono(Void.class);
    }
}
