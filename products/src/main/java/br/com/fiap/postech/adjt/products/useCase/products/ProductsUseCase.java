package br.com.fiap.postech.adjt.products.useCase.products;

import br.com.fiap.postech.adjt.products.infrastructure.products.controller.dto.ItemResponseDTO;

public interface ProductsUseCase {

    ItemResponseDTO busca(final Long itemId,
                          final Long quantity);

}
