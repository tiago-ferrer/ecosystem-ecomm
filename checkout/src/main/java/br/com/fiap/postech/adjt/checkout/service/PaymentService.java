package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentResponseDto;
import br.com.fiap.postech.adjt.checkout.entity.payment.Payment;

public interface PaymentService {
    public Payment createPayment(Payment payment);
    PaymentResponseDto validarPayment(PaymentResponseDto responseDto);
}
