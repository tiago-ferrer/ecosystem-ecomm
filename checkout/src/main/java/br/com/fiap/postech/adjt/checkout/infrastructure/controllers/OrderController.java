package br.com.fiap.postech.adjt.checkout.infrastructure.controllers;

import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.postech.adjt.checkout.infrastructure.brokers.CheckoutWithStreamBridge;
import br.com.fiap.postech.adjt.checkout.infrastructure.client.GetCartClient;
import br.com.fiap.postech.adjt.checkout.infrastructure.controllers.exceptions.ValidationTrigger;
import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.BeforeSave;
import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.CheckoutRequest;
import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.ErrorResponse;
import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.GetCartPayload;
import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.GetCartResponse;
import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.PaymentConsumerPayload;
import br.com.fiap.postech.adjt.checkout.infrastructure.persistance.gateways.OrderGateway;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("carts")
@AllArgsConstructor
public class OrderController {
    Pattern UUID_REGEX = Pattern
            .compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    private final OrderGateway orderGateway;
    private final CheckoutWithStreamBridge checkoutWithStreamBridge;
    private final GetCartClient getCartClient;

    @PostMapping()
    public ResponseEntity checkout(@RequestBody() @Valid CheckoutRequest checkoutRequest, BindingResult bindingResult) {
        new ValidationTrigger(bindingResult).verify();
        if (!UUID_REGEX.matcher(checkoutRequest.consumerId().toString()).matches()) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Invalid orderId format"));
        }
        GetCartResponse cart = getCartClient.exec(new GetCartPayload(checkoutRequest.consumerId()));
        if (cart.items().isEmpty()) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Empty cart"));
        }
        BeforeSave response = orderGateway.checkout(checkoutRequest, cart.items());
        checkoutWithStreamBridge
                .sendCheckoutEvent(new PaymentConsumerPayload(checkoutRequest.consumerId(), response.orderId()));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity findByConsumerId(@PathVariable UUID id) {
        if (!UUID_REGEX.matcher(id.toString()).matches()) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Invalid consumerId format"));
        }
        return ResponseEntity.ok(orderGateway.findByConsumerId(id));
    }

    @GetMapping("/by-order-id/{id}")
    public ResponseEntity findByOrderId(@PathVariable UUID id) {
        if (!UUID_REGEX.matcher(id.toString()).matches()) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Invalid consumerId format"));
        }
        return ResponseEntity.ok(orderGateway.findByOrderId(id));
    }
}
