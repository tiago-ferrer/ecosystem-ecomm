package br.com.fiap.postech.adjt.checkout.useCase.checkout;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OrderAsyncUseCase {

    private final CheckoutUseCase service;

    public OrderAsyncUseCase(final CheckoutUseCase service) {
        this.service = service;
    }

    //TODO: Executa de 5 em 5 minutos, no minuto 00. Exemplo: 20:05 / 20:10 / 20:15 / 20:20
    @Scheduled(cron = "0 0/5 * * * ?")
    public void verificaPagamentosPendentes() {
        this.service.processaPendentes();
    }

}
