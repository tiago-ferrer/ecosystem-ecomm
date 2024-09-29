package br.com.fiap.postech.adjt.checkout.controller;

import br.com.fiap.postech.adjt.checkout.dto.checkout.CheckoutDto;
import br.com.fiap.postech.adjt.checkout.dto.checkout.CheckoutResponse;
import br.com.fiap.postech.adjt.checkout.exception.CartConsumerException;
import br.com.fiap.postech.adjt.checkout.producer.PaymentProducer;
import br.com.fiap.postech.adjt.checkout.service.CheckoutService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService service;

    public CheckoutController(CheckoutService service) {
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<CheckoutResponse> addCheckout(@RequestBody @Valid CheckoutDto checkout) {
        return ResponseEntity.ok(service.createChekout(checkout));
    }


    @GetMapping("/{consumerId}")
    public ResponseEntity<?> getOrdersByConsumer(@PathVariable String consumerId){
        try{
            var checkoutConsumer =  service.getOrdersByConsumer(consumerId);
            return ResponseEntity.ok(checkoutConsumer);
        }catch (CartConsumerException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


}
