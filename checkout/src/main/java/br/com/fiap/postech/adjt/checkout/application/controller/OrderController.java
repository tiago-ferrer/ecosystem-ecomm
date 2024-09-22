package br.com.fiap.postech.adjt.checkout.application.controller;

import br.com.fiap.postech.adjt.checkout.application.controller.facade.OrderFacade;
import br.com.fiap.postech.adjt.checkout.application.dto.CheckoutDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.CheckoutResponseDTO;
import br.com.fiap.postech.adjt.checkout.application.dto.OrderDTO;
import br.com.fiap.postech.adjt.checkout.domain.exception.AppException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkout")
public class OrderController {

    private final OrderFacade orderFacade;

    public OrderController(OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @PostMapping
    public CheckoutResponseDTO createOrder(@Valid @RequestBody CheckoutDTO checkoutDTO) throws AppException {
        return orderFacade.createOrder(checkoutDTO);
    }

    @GetMapping("/by-order-id/{orderId}")
    public OrderDTO getOrder(@PathVariable String orderId) throws AppException {
        return orderFacade.getOrderById(orderId);
    }

    @GetMapping("/{customerId}")
    public List<OrderDTO> getOrderByCustomerId(@PathVariable String customerId) throws AppException {
        return orderFacade.getOrderByCustomerId(customerId);
    }
}
