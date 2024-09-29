package br.com.fiap.postech.adjt.checkout.producer;

import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentRequestDto;

public interface PaymentEvent {
    void sendPaymentCreateEvent(PaymentRequestDto paymentRequestDto);
}
