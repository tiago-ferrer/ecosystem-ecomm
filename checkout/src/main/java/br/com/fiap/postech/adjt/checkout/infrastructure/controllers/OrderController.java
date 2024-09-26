//package br.com.fiap.postech.adjt.checkout.infrastructure.controllers;
//
//import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.CheckoutRequest;
//import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.CheckoutResponse;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("checkout")
//public class OrderController {
//
//    @PostMapping
//    public CheckoutResponse checkout(
//            @RequestBody CheckoutRequest dto) {
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(this.clienteService.create(dto.nome()));
//    }
//}
