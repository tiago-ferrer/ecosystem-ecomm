package br.com.fiap.postech.adjt.checkout.dataprovider.integration.cart;

import br.com.fiap.postech.adjt.checkout.application.dto.PaymentResponseDTO;
import br.com.fiap.postech.adjt.checkout.domain.exception.AppException;
import br.com.fiap.postech.adjt.checkout.domain.exception.constants.ErrorConstants;
import br.com.fiap.postech.adjt.checkout.domain.gateway.CartGateway;
import br.com.fiap.postech.adjt.checkout.domain.model.cart.CartModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class CartGatewayClientImpl implements CartGateway {

    @Value("${app.cart-base-url}")
    private String cartBaseUrl;

    @Override
    public CartModel getCartByConsumerId(String consumerId) {

        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        String body = "{ \"consumerId\": \"" + consumerId + "\" }";
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            URI uri = URI.create(cartBaseUrl + "/cart");
            RequestEntity<String> requestEntity = new RequestEntity<>(body, HttpMethod.GET, uri);
            ResponseEntity<CartModel> responseEntity = client.exchange(requestEntity, CartModel.class);

            return responseEntity.getBody();
            //ResponseEntity<CartModel> response = client.exchange(cartBaseUrl+"/cart", HttpMethod.GET, entity, CartModel.class);
            /*
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new AppException(ErrorConstants.CART_ITEMS_EMPTY);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl(cartBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

    }

}
