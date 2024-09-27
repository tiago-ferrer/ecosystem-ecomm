package br.com.fiap.postech.adjt.checkout.integracao;

import br.com.fiap.postech.adjt.checkout.consumer.ApiPayment;
import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentRequestDto;
import br.com.fiap.postech.adjt.checkout.dto.payment.PaymentResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ApiPaymentService {
    private final ApiPayment apiPayment;

    public ApiPaymentService(ApiPayment apiPayment) {
        this.apiPayment = apiPayment;
    }

    @Async
    public CompletableFuture<Void> callExternalApi(PaymentRequestDto paymentRequest) {
         PaymentResponseDto paymentResponse = apiPayment.createPayment(paymentRequest, "c92cc63d352a244f31f87f09832a9abfc47aed2f0e679a3a052fd7a3164711b0");

        try {
            Thread.sleep(5000);
            log.info("Resposta da API Pagamento sendo processada.");
        } catch (InterruptedException e) {
            log.error("Erro ao chamar API pagamento", e);
        }
        return CompletableFuture.completedFuture(null);
    }
}
