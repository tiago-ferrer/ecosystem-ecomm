package br.com.fiap.postech.adjt.cart.useCase.cart.impl;

import br.com.fiap.postech.adjt.cart.domain.cart.Consumer;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto.AdicionaItemRequestDTO;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto.AdicionaItemResponseDTO;
import br.com.fiap.postech.adjt.cart.useCase.cart.CartUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class CartUseCaseImpl implements CartUseCase {


    @Override
    public AdicionaItemResponseDTO adiciona(final AdicionaItemRequestDTO dadosItem) {
        final var consumerId = new Consumer(dadosItem.consumerId());

        return new AdicionaItemResponseDTO(
                "Item added to cart successfully"
        );
    }

}
