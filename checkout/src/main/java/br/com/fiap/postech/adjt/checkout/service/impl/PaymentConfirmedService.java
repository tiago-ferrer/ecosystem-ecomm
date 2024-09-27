package br.com.fiap.postech.adjt.checkout.service.impl;

import br.com.fiap.postech.adjt.checkout.entity.payment.PaymentConfirmed;
import br.com.fiap.postech.adjt.checkout.repository.PaymentConfirmedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentConfirmedService {

    @Autowired
    private PaymentConfirmedRepository repository;

    public void cadastrar(PaymentConfirmed paymentConfirmed){
         repository.save(paymentConfirmed);
    }

}
