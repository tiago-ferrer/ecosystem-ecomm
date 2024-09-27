package br.com.fiap.postech.adjt.payment.domain.useCases;

import br.com.fiap.postech.adjt.payment.infrastructure.client.GetTokenClient;
import br.com.fiap.postech.adjt.payment.infrastructure.client.ProcessPaymentClient;
import br.com.fiap.postech.adjt.payment.infrastructure.dtos.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PaymentRequestUseCase {
    @Value("${api.security.token}")
    private String token;

    @Value("${api.security.token.group}")
    private String group;

    @Value("${api.security.token.members}")
    private List<String> members;

    private final GetTokenClient getTokenClient;
    private final ProcessPaymentClient processPaymentClient;

    public PaymentResponse exec(PaymentRequest paymentRequest) {
        try {
            return  processPaymentClient.exec(paymentRequest, token);
        } catch(Exception e) {
           GetTokenResponse response = getTokenClient.exec(new GetTokenPayload(this.group, this.members));
           this.token = response.apiKey();
           return  processPaymentClient.exec(paymentRequest, response.apiKey());
        }
    }

}
