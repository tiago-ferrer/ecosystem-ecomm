package br.com.fiap.postech.adjt.payment.domain.useCases;

import br.com.fiap.postech.adjt.payment.infrastructure.client.GetTokenClient;
import br.com.fiap.postech.adjt.payment.infrastructure.client.ProcessPaymentClient;
import br.com.fiap.postech.adjt.payment.infrastructure.dtos.GetTokenPayload;
import br.com.fiap.postech.adjt.payment.infrastructure.dtos.GetTokenResponse;
import br.com.fiap.postech.adjt.payment.infrastructure.dtos.PaymentRequest;
import br.com.fiap.postech.adjt.payment.infrastructure.dtos.PaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class PaymentRequestUseCase {

    @Autowired
    private GetTokenClient getTokenClient;
    @Autowired
    private ProcessPaymentClient processPaymentClient;

    @Value("${api.security.token.secret}")
    private String token;

    @Value("${api.security.group}")
    private String group;

    public PaymentResponse exec(PaymentRequest paymentRequest) {
        try {
            return  processPaymentClient.exec(paymentRequest, this.token);
        } catch(Exception e) {
           List<String> list = new ArrayList<>(5);
           list.add("Rafael Amaral");
           list.add("André S Ferreira");
           list.add("Giulliana Munhoz");
           list.add("Tiago Santana");
           list.add("Eduardo Vilhena");
           GetTokenResponse response = getTokenClient.exec( new GetTokenPayload(this.group, list));
           String token = response.apiKey();
           return  processPaymentClient.exec(paymentRequest, token);
        }
    }

}
