package br.com.fiap.postech.adjt.checkout.domain.entities.useCases;

import br.com.fiap.postech.adjt.checkout.domain.entities.enums.OrderStatus;
import br.com.fiap.postech.adjt.checkout.infrastructure.client.GetTokenClient;
import br.com.fiap.postech.adjt.checkout.infrastructure.client.ProcessPaymentClient;
import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.GetTokenPayload;
import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.GetTokenResponse;
import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.PaymentRequest;
import br.com.fiap.postech.adjt.checkout.infrastructure.dtos.PaymentResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@AllArgsConstructor
public class PaymentRequestUseCase {


    private final GetTokenClient getTokenClient;
    private final ProcessPaymentClient processPaymentClient;

    public PaymentResponse exec(PaymentRequest paymentRequest) {
        try {
        String token = "b647323e4680065537924fb9876da1f0fd71f6558640e847dd8ea39c72a8ee46";
            return  processPaymentClient.exec(paymentRequest, token);
        } catch(Exception e) {
            try {
            log.info("Ocorreu um erro na requisição");
            List<String> list = new ArrayList<>(5);
            list.add("Rafael Amaral");
            list.add("André S Ferreira");
            list.add("Giulliana Munhoz");
            list.add("Tiago Santana");
            list.add("Eduardo Vilhena");
            GetTokenResponse response = getTokenClient.exec( new GetTokenPayload("Grupo20", list));
            String token = response.apiKey();
            return  processPaymentClient.exec(paymentRequest, token);
            } catch(Exception err) {
                log.info("Ocorreu um erro ao pegar o token tamém");
                return new PaymentResponse(paymentRequest.orderId(), OrderStatus.declined);
            }
        }
    }

}
