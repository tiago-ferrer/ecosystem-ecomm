package br.com.fiap.postech.adjt.products.useCase.products.impl;

import br.com.fiap.postech.adjt.products.domain.products.Item;
import br.com.fiap.postech.adjt.products.infrastructure.fakeStoreApi.client.FakeStoreApiClient;
import br.com.fiap.postech.adjt.products.infrastructure.fakeStoreApi.client.response.ItemDTO;
import br.com.fiap.postech.adjt.products.infrastructure.products.controller.dto.ItemResponseDTO;
import br.com.fiap.postech.adjt.products.useCase.products.ProductsUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@Slf4j
public class ProductsUseCaseImpl implements ProductsUseCase {

    private final FakeStoreApiClient fakeStoreApiClient;

    public ProductsUseCaseImpl(final FakeStoreApiClient fakeStoreApiClient) {
        this.fakeStoreApiClient = fakeStoreApiClient;
    }

    @Override
    public ItemResponseDTO busca(final Long itemId,
                                 final Long quantity) {
        final var itemObject = new Item(quantity);
        ItemDTO item = null;
        try {
            item = this.fakeStoreApiClient.pegaItem(itemId);
        } catch (Exception e) {
            log.error("Problemas com a API de ITEM ", e);
            throw new IllegalArgumentException("Problemas com a API de ITEM");
        }
        if(Objects.isNull(item)) {
            log.error("Item não encontrado");
            throw new IllegalArgumentException("Item não encontrado");
        }
        return null;
    }
}
