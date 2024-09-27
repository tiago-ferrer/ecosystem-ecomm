package br.com.fiap.postech.adjt.checkout.service;

import br.com.fiap.postech.adjt.checkout.model.Order;
import br.com.fiap.postech.adjt.checkout.model.dto.request.PaymentRequest;

public interface PaymentService {
    void process(Order order, PaymentRequest paymentRequest);
}
