package br.com.fiap.postech.adjt.checkout.useCase.checkout;

import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto.BuscaListaPagamentoResponseDTO;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto.BuscaPagamentoResponseDTO;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto.PagamentoResponseDTO;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto.SolicitaPagamentoRequestDTO;

public interface CheckoutUseCase {

    PagamentoResponseDTO processa(final SolicitaPagamentoRequestDTO dadosPagamento);

    void processaPendentes();

    BuscaListaPagamentoResponseDTO busca(final String consumerId);

    BuscaPagamentoResponseDTO buscaPorOrderId(final String orderId);
}
