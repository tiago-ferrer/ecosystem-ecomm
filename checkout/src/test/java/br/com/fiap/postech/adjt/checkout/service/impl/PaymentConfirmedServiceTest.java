package br.com.fiap.postech.adjt.checkout.service.impl;

import br.com.fiap.postech.adjt.checkout.TestUtils;
import br.com.fiap.postech.adjt.checkout.repository.PaymentConfirmedRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentConfirmedServiceTest {

    @InjectMocks
    private PaymentConfirmedService paymentConfirmedService;

    @Mock
    private PaymentConfirmedRepository repository;

    @Test
    void testCadSuccess() {
        paymentConfirmedService.cadastrar(TestUtils.buildPaymentConfirmed());
        verify(repository, times(1)).save(Mockito.any());
    }

}
