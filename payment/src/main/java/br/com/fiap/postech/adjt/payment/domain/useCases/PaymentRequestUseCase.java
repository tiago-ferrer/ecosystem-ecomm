package br.com.fiap.postech.adjt.payment.domain.useCases;

import br.com.fiap.postech.adjt.payment.infrastructure.client.GetTokenClient;
import br.com.fiap.postech.adjt.payment.infrastructure.client.ProcessPaymentClient;
import br.com.fiap.postech.adjt.payment.infrastructure.dtos.PaymentRequest;
import br.com.fiap.postech.adjt.payment.infrastructure.dtos.PaymentResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentRequestUseCase {
    private final GetTokenClient getTokenClient;
    private final ProcessPaymentClient processPaymentClient;

    public PaymentResponse exec(PaymentRequest paymentRequest) {
            String token = "b647323e4680065537924fb9876da1f0fd71f6558640e847dd8ea39c72a8ee46";
            return  processPaymentClient.exec(paymentRequest, token);
    }

}
