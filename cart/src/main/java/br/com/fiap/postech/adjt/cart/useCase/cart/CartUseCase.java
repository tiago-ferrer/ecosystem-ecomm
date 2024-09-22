package br.com.fiap.postech.adjt.cart.useCase.cart;

import br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto.AdicionaItemRequestDTO;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto.AdicionaItemResponseDTO;

public interface CartUseCase {

    AdicionaItemResponseDTO adiciona(final AdicionaItemRequestDTO dadosItem);

}
