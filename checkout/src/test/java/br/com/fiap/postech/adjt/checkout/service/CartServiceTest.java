//package br.com.fiap.postech.adjt.checkout.service;
//
//import br.com.fiap.postech.adjt.checkout.dto.ConsumerIdRequest;
//import br.com.fiap.postech.adjt.checkout.model.Cart;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpMethod;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//import reactor.test.StepVerifier;
//
//import java.util.UUID;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//class CartServiceTest {
//
//    @Mock
//    private WebClient webClient;
//
//    @Mock
//    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
//
//    @Mock
//    private WebClient.RequestBodySpec requestBodySpec;
//
//    @Mock
//    private WebClient.ResponseSpec responseSpec;
//
//    @InjectMocks
//    private CartService cartService;
//
//    private UUID consumerId;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        consumerId = UUID.randomUUID();
//
//        // Mocking WebClient behavior
//        when(webClient.method(eq(HttpMethod.GET))).thenReturn(requestBodyUriSpec);
//        when(webClient.method(eq(HttpMethod.DELETE))).thenReturn(requestBodyUriSpec);
//        when(requestBodyUriSpec.uri(eq("/"))).thenReturn(requestBodySpec);
//        when(requestBodySpec.bodyValue(any())).thenReturn(requestBodySpec);
//    }
//
//    @Test
//    void getCart_ShouldReturnCart() {
//        Cart mockCart = new Cart(/* parâmetros do Cart */);
//        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
//        when(responseSpec.bodyToMono(Cart.class)).thenReturn(Mono.just(mockCart));
//
//        // Act
//        Mono<Cart> result = cartService.getCart(consumerId);
//
//        // Assert
//        StepVerifier.create(result)
//                .expectNext(mockCart)
//                .verifyComplete();
//
//        // Verify interactions
//        verify(webClient).method(HttpMethod.GET);
//        verify(requestBodySpec).bodyValue(any(ConsumerIdRequest.class));
//        verify(requestBodySpec).retrieve();
//    }
//
//    @Test
//    void clearCart_ShouldClearCart() {
//        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
//        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());
//
//        // Act
//        cartService.clearCart(consumerId);
//
//        // Verify interactions
//        verify(webClient).method(HttpMethod.DELETE);
//        verify(requestBodySpec).bodyValue(any(ConsumerIdRequest.class));
//        verify(requestBodySpec).retrieve();
//    }
//}