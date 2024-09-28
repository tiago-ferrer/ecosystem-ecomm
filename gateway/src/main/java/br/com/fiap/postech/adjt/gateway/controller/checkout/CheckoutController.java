package br.com.fiap.postech.adjt.gateway.controller.checkout;

import br.com.fiap.postech.adjt.gateway.controller.checkout.request.CreateOrderRequest;
import br.com.fiap.postech.adjt.gateway.controller.checkout.response.CreateOrderResponse;
import br.com.fiap.postech.adjt.gateway.controller.checkout.response.GetOrdersByConsumerIdResponse;
import br.com.fiap.postech.adjt.gateway.controller.checkout.response.OrderResponse;
import br.com.fiap.postech.adjt.gateway.controller.response.ErrorResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final RestClient restClient = RestClient.create();
    private final String checkoutURI = "http://checkout:8082";

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody @Valid CreateOrderRequest createOrderRequest) {
        try {
            return restClient
                    .method(HttpMethod.POST)
                    .uri(checkoutURI)
                    .contentType(APPLICATION_JSON)
                    .body(createOrderRequest)
                    .retrieve()
                    .toEntity(CreateOrderResponse.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().is4xxClientError()) {
                return ResponseEntity.status(e.getStatusCode()).body(new ErrorResponse(e.getResponseBodyAsString()));
            }
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @GetMapping("/{consumerId}")
    public ResponseEntity<?> findOrdersByConsumerId(@PathVariable("consumerId") String consumerId) {
        try {
            return restClient
                    .method(HttpMethod.GET)
                    .uri(checkoutURI + "/" + consumerId)
                    .retrieve()
                    .toEntity(GetOrdersByConsumerIdResponse.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().is4xxClientError()) {
                return ResponseEntity.status(e.getStatusCode()).body(new ErrorResponse(e.getResponseBodyAsString()));
            }
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @GetMapping("/by-cart-id/{orderId}")
    public ResponseEntity<?> findOrderById(@PathVariable("orderId") String orderId) {
        try {
            return restClient
                    .method(HttpMethod.GET)
                    .uri(checkoutURI + "/by-cart-id/" + orderId)
                    .retrieve()
                    .toEntity(OrderResponse.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().is4xxClientError()) {
                return ResponseEntity.status(e.getStatusCode()).body(new ErrorResponse(e.getResponseBodyAsString()));
            }
            return ResponseEntity.internalServerError().body(e);
        }
    }

}
