package br.com.fiap.postech.adjt.checkout.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.fiap.postech.adjt.checkout.model.OrderEntity;
import br.com.fiap.postech.adjt.checkout.model.request.CheckoutRequest;
import br.com.fiap.postech.adjt.checkout.model.response.CheckoutResponse;
import br.com.fiap.postech.adjt.checkout.model.response.OrderCheckoutsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RequestMapping("/checkout")
public interface CheckoutController {

    @Operation(
            summary = "Executa o pagamento",
            description = "Endpoint voltado pagamento de Carrinhos de Compras que estejam integros para conclusão..",
            responses = {
                    @ApiResponse(responseCode = "200", description = "ok."),
                    @ApiResponse(responseCode = "400", description = "Bad request."),
            }
    )
    @PostMapping("/")
    ResponseEntity<CheckoutResponse> createCheckout(@Valid @RequestBody CheckoutRequest checkoutRequest);

    @GetMapping("/{consumerId}")
    ResponseEntity<List<OrderCheckoutsResponse>> getOrdersByConsumerId(@PathVariable UUID consumerId);
    
    @GetMapping("/by-order-id/{orderId}")
    ResponseEntity<OrderEntity> getOrderById(@PathVariable UUID orderId);

}