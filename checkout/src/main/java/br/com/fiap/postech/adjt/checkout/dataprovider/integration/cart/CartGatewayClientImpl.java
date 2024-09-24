package br.com.fiap.postech.adjt.checkout.dataprovider.integration.cart;

import br.com.fiap.postech.adjt.checkout.dataprovider.integration.dto.CartByConsumerDTO;
import br.com.fiap.postech.adjt.checkout.dataprovider.integration.mappers.CartMapper;
import br.com.fiap.postech.adjt.checkout.domain.gateway.CartGateway;
import br.com.fiap.postech.adjt.checkout.domain.model.cart.CartModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartGatewayClientImpl implements CartGateway {

    private final WebClient webClientCart;
    private final CartMapper cartMapper;

    @Value("${app.cart-base-url}")
    private String cartBaseUrl;

    @Override
    public CartModel getCartByConsumerId(String consumerId) {
        Mono<CartByConsumerDTO> cartByConsumerDTO = this.webClientCart
                .method(HttpMethod.GET)
                .uri("/cart")
                .bodyValue("{\"consumerId\":\"" + consumerId + "\"}")
                .retrieve()
                .bodyToMono(CartByConsumerDTO.class);

        //fluxo bloqueante
        CartByConsumerDTO cart = cartByConsumerDTO.block();

        return new CartModel().builder()
                .items(cart.getItems().stream().map(cartMapper::toCartItensModel).toList())
                .build();
    }

    @Override
    public void emptyCartByConsumerId(String consumerId) {

        Mono<Void> cartByConsumerDTO = webClientCart()
                .method(HttpMethod.DELETE)
                .uri("/cart")
                .bodyValue("{\"consumerId\":\"" + consumerId + "\"}")
                .retrieve()
                .bodyToMono(Void.class);

        //fluxo bloqueante
        cartByConsumerDTO.block();
    }

    private WebClient webClientCart() {
        return WebClient.builder()
                .baseUrl(cartBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
