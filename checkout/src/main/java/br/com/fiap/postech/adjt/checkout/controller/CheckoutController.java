package br.com.fiap.postech.adjt.checkout.controller;

import br.com.fiap.postech.adjt.checkout.dto.CheckoutRequestDTO;
import br.com.fiap.postech.adjt.checkout.dto.CheckoutResponseDTO;
import br.com.fiap.postech.adjt.checkout.dto.OrderResponseDTO;
import br.com.fiap.postech.adjt.checkout.service.CheckoutService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public CheckoutResponseDTO processPayment(@RequestBody CheckoutRequestDTO checkoutRequestDTO) {
        return checkoutService.processPayment(checkoutRequestDTO);

    }

    @GetMapping("/by-order-id/{orderId}")
    public OrderResponseDTO searchPaymentByOrderId(@PathVariable String orderId) {
        return checkoutService.searchPaymentByOrderId(orderId);
    }

    @GetMapping("/{consumerId}")
    public List<OrderResponseDTO> searchPaymentByConsumer(@PathVariable String consumerId) {
        return checkoutService.searchPaymentByConsumer(consumerId);
    }


}
