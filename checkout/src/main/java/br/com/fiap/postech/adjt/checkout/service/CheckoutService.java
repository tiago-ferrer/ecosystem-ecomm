package br.com.fiap.postech.adjt.checkout.service;


import br.com.fiap.postech.adjt.checkout.dto.checkout.CheckoutConsumerDto;
import br.com.fiap.postech.adjt.checkout.dto.checkout.CheckoutDto;
import br.com.fiap.postech.adjt.checkout.dto.checkout.CheckoutResponse;
import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentRequestDto;
import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentResponseDto;

public interface CheckoutService {
    CheckoutResponse createChekout(CheckoutDto checkout);
    CheckoutConsumerDto getOrdersByConsumer(String consumerId);
}
