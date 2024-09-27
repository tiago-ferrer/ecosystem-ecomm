package br.com.fiap.postech.adjt.checkout.controller.impl;

import br.com.fiap.postech.adjt.checkout.controller.CheckoutController;
import br.com.fiap.postech.adjt.checkout.model.OrderEntity;
import br.com.fiap.postech.adjt.checkout.model.request.CheckoutRequest;
import br.com.fiap.postech.adjt.checkout.model.response.CheckoutResponse;
import br.com.fiap.postech.adjt.checkout.model.response.OrderCheckoutsResponse;
import br.com.fiap.postech.adjt.checkout.service.CheckoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/checkout")
public class CheckoutControllerImpl implements CheckoutController {

    private final CheckoutService checkoutService;

    @Autowired
    public CheckoutControllerImpl(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }


    @Override
    @Operation(
            summary = "Executa o pagamento",
            description = "Endpoint voltado pagamento de Carrinhos de Compras que estejam integros para conclusão..",
            responses = {
                    @ApiResponse(responseCode = "200", description = "ok."),
                    @ApiResponse(responseCode = "400", description = "Bad request."),
            }
    )
    @PostMapping
    public ResponseEntity<CheckoutResponse> createCheckout(CheckoutRequest checkoutRequest) {
        checkoutRequest.validateConsumerId();

        CheckoutResponse checkoutResponse = checkoutService.processCheckout(
                UUID.fromString(checkoutRequest.getConsumerId()),
                checkoutRequest.getAmount(),
                checkoutRequest.getCurrency(),
                checkoutRequest.getPaymentMethod()
        );
        return ResponseEntity.ok(checkoutResponse);
    }

    @Override
    @GetMapping("/{consumerId}")
    public ResponseEntity<List<OrderCheckoutsResponse>> getOrdersByConsumerId(UUID consumerId) {
        List<OrderCheckoutsResponse> orders = checkoutService.getOrdersByConsumerId(consumerId);
        return ResponseEntity.ok(orders);
    }

    @Override
    @GetMapping("/by-order-id/{orderId}")
    public ResponseEntity<OrderEntity> getOrderById(UUID orderId) {
        OrderEntity order = checkoutService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }
}