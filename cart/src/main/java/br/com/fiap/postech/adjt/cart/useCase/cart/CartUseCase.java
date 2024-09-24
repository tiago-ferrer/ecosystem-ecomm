package br.com.fiap.postech.adjt.cart.useCase.cart;

import br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto.*;

public interface CartUseCase {

    ItemResponseDTO adiciona(final AdicionaItemRequestDTO dadosItem);

    ItemResponseDTO deleta(final ItemAndConsumerIdRequestDTO dadosItem);

    ItemResponseDTO atualiza(final ItemAndConsumerIdRequestDTO dadosItem);

    InfoItensResponseDTO busca(final ConsumerIdRequestDTO consumer);

    ItemResponseDTO deletaOCarrinho(final ConsumerIdRequestDTO consumer);

}
