package br.com.fiap.postech.adjt.cart.useCase.cart;

import br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto.AdicionaItemRequestDTO;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto.DeletaItemRequestDTO;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto.ItemResponseDTO;

public interface CartUseCase {

    ItemResponseDTO adiciona(final AdicionaItemRequestDTO dadosItem);

    ItemResponseDTO deleta(final DeletaItemRequestDTO dadosItem);

}
