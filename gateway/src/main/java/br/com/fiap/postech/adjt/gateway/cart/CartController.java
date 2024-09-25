package br.com.fiap.postech.adjt.gateway.cart;

import br.com.fiap.postech.adjt.gateway.cart.request.AddItemRequest;
import br.com.fiap.postech.adjt.gateway.cart.request.ChangeItemRequest;
import br.com.fiap.postech.adjt.gateway.cart.request.FindCartRequest;
import br.com.fiap.postech.adjt.gateway.cart.request.RemoveAllItemsRequest;
import br.com.fiap.postech.adjt.gateway.cart.response.CartResponse;
import br.com.fiap.postech.adjt.gateway.response.ErrorResponse;
import br.com.fiap.postech.adjt.gateway.response.MessageResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final RestClient restClient = RestClient.create();
    private final String cartURI = "http://cart:8081";

    @PostMapping("/items")
    public ResponseEntity<?> addItem(@RequestBody AddItemRequest addItemRequest) {
        try {
            return restClient
                    .method(HttpMethod.POST)
                    .uri(cartURI + "/items")
                    .contentType(APPLICATION_JSON)
                    .body(addItemRequest)
                    .retrieve()
                    .toEntity(MessageResponse.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().is4xxClientError()) {
                return ResponseEntity.status(e.getStatusCode()).body(new ErrorResponse(e.getResponseBodyAsString()));
            }
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @DeleteMapping("/item")
    public ResponseEntity<?> removeItem(@RequestBody ChangeItemRequest changeItemRequest) {
        try {
            return restClient
                    .method(HttpMethod.DELETE)
                    .uri(cartURI + "/item")
                    .contentType(APPLICATION_JSON)
                    .body(changeItemRequest)
                    .retrieve()
                    .toEntity(MessageResponse.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().is4xxClientError()) {
                return ResponseEntity.status(e.getStatusCode()).body(new ErrorResponse(e.getResponseBodyAsString()));
            }
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @PutMapping("/item")
    public ResponseEntity<?> incrementItem(@RequestBody ChangeItemRequest changeItemRequest) {
        try {
            return restClient
                    .method(HttpMethod.PUT)
                    .uri(cartURI + "/item")
                    .contentType(APPLICATION_JSON)
                    .body(changeItemRequest)
                    .retrieve()
                    .toEntity(MessageResponse.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().is4xxClientError()) {
                return ResponseEntity.status(e.getStatusCode()).body(new ErrorResponse(e.getResponseBodyAsString()));
            }
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @GetMapping
    public ResponseEntity<?> findCart(@RequestBody FindCartRequest findCartRequest) {
        try {
            return restClient
                    .method(HttpMethod.GET)
                    .uri(cartURI)
                    .contentType(APPLICATION_JSON)
                    .body(findCartRequest)
                    .retrieve()
                    .toEntity(CartResponse.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().is4xxClientError()) {
                return ResponseEntity.status(e.getStatusCode()).body(new ErrorResponse(e.getResponseBodyAsString()));
            }
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> removeAllItems() {
        return ResponseEntity.status(HttpStatusCode.valueOf(404)).build();
//        try {
//            return restClient
//                    .method(HttpMethod.DELETE)
//                    .uri(cartURI)
//                    .contentType(APPLICATION_JSON)
//                    .body(removeAllItemsRequest)
//                    .retrieve()
//                    .toEntity(MessageResponse.class);
//        } catch (HttpClientErrorException e) {
//            if (e.getStatusCode().is4xxClientError()) {
//                return ResponseEntity.status(e.getStatusCode()).body(new ErrorResponse(e.getResponseBodyAsString()));
//            }
//            return ResponseEntity.internalServerError().body(e);
//        }
    }

}
