package br.com.fiap.postech.adjt.checkout.controller;

import br.com.fiap.postech.adjt.checkout.dto.CheckoutRequestDTO;
import br.com.fiap.postech.adjt.checkout.dto.CheckoutResponseDTO;
import br.com.fiap.postech.adjt.checkout.dto.OrderDTO;
import br.com.fiap.postech.adjt.checkout.service.CheckoutService;
import br.com.fiap.postech.adjt.checkout.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final OrderService orderService;

    public CheckoutController(CheckoutService checkoutService, OrderService orderService) {
        this.checkoutService = checkoutService;
        this.orderService = orderService;
    }

    @PostMapping
    public CheckoutResponseDTO processPayment(@RequestBody @Valid CheckoutRequestDTO checkoutRequestDTO) {
        return checkoutService.processPayment(checkoutRequestDTO);

    }

    @GetMapping("/by-order-id/{orderId}")
    public OrderDTO searchPaymentByOrderId(@PathVariable String orderId) {
        return orderService.findByOrderId(orderId);
    }

    @GetMapping("/{consumerId}")
    public List<OrderDTO> searchPaymentByConsumer(@PathVariable String consumerId) {
        return orderService.findPaymentByConsumer(consumerId);
    }


}
