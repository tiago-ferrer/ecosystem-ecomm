package br.com.fiap.postech.adjt.checkout.useCase.checkout;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OrderAsyncUseCase {

    private final CheckoutUseCase service;

    public OrderAsyncUseCase(final CheckoutUseCase service) {
        this.service = service;
    }

    //TODO: Executa de 2 em 2 minutos, no minuto 00. Exemplo: 20:02 / 20:04 / 20:06 / 20:08
    @Scheduled(cron = "0 0/2 * * * ?")
    public void verificaPagamentosPendentes() {
        this.service.processaPendentes();
    }

}
