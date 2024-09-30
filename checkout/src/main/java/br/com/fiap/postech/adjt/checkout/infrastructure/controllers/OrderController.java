package br.com.fiap.postech.adjt.checkout.infrastructure.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.postech.adjt.checkout.domain.entities.Item;
import br.com.fiap.postech.adjt.checkout.infrastructure.brokers.CheckoutWithStreamBridge;
import br.com.fiap.postech.adjt.checkout.infrastructure.client.GetCartClient;
import br.com.fiap.postech.adjt.checkout.infrastructure.controllers.exceptions.ValidationTrigger;
import br.com.fiap.postech.adjt.checkout.infrastructure.persistance.gateways.OrderGateway;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("checkout")
@AllArgsConstructor
public class OrderController {

    private final OrderGateway orderGateway;
    private final CheckoutWithStreamBridge checkoutWithStreamBridge;
    private final GetCartClient getCartClient;

    @PostMapping()
    public ResponseEntity checkout(@RequestBody() CheckoutRequest checkoutRequest, BindingResult bindingResult) {
        Pattern UUID_REGEX = Pattern
                .compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
        new ValidationTrigger(bindingResult).verify();
        if (!UUID_REGEX.matcher(checkoutRequest.consumerId().toString()).matches()) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Invalid orderId format"));
        }
        GetCartResponse cartResponse = getCartClient.exec(new GetCartPayload(checkoutRequest.consumerId()));

        BeforeSave response = orderGateway.checkout(checkoutRequest, cartResponse.items());
        checkoutWithStreamBridge
                .sendCheckoutEvent(new PaymentConsumerPayload(checkoutRequest.consumerId(), response.orderId()));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity findByConsumerId(@PathVariable UUID id) {
        Pattern UUID_REGEX = Pattern
                .compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
        if (!UUID_REGEX.matcher(id.toString()).matches()) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Invalid consumerId format"));
        }
        return ResponseEntity.ok(orderGateway.findByConsumerId(id));
    }

    @GetMapping("/by-order-id/{id}")
    public ResponseEntity findByOrderId(@PathVariable UUID id) {
        Pattern UUID_REGEX = Pattern
                .compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
        if (!UUID_REGEX.matcher(id.toString()).matches()) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Invalid consumerId format"));
        }
        return ResponseEntity.ok(orderGateway.findByOrderId(id));
    }
}
