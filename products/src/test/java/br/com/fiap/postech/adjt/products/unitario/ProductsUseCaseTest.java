package br.com.fiap.postech.adjt.products.unitario;

import br.com.fiap.postech.adjt.products.infrastructure.fakeStoreApi.client.FakeStoreApiClient;
import br.com.fiap.postech.adjt.products.infrastructure.fakeStoreApi.client.response.ItemDTO;
import br.com.fiap.postech.adjt.products.useCase.products.impl.ProductsUseCaseImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ProductsUseCaseTest {

    @Test
    public void busca_sucesso() {
        // preparação
        var fakeStoreApiClient = Mockito.mock(FakeStoreApiClient.class);

        Mockito.when(fakeStoreApiClient.pegaItem(Mockito.any()))
                .thenReturn(
                        new ItemDTO(
                                1L,
                                "Item Teste",
                                new BigDecimal("100"),
                                "Category Teste",
                                "Description Teste",
                                "https://teste.com.br"
                        )
                );

        var service = new ProductsUseCaseImpl(fakeStoreApiClient);

        // execução
        service.busca(1L, 1L);

        // avaliação
        verify(fakeStoreApiClient, times(1)).pegaItem(Mockito.any());
    }

    @Test
    public void busca_itemNaoExiste() {
        // preparação
        var fakeStoreApiClient = Mockito.mock(FakeStoreApiClient.class);

        Mockito.when(fakeStoreApiClient.pegaItem(Mockito.any()))
                .thenReturn(
                        null
                );

        var service = new ProductsUseCaseImpl(fakeStoreApiClient);

        // execução e avaliação
        var excecao = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.busca(1L, 1L);
        });

        verify(fakeStoreApiClient, times(1)).pegaItem(Mockito.any());
        Assertions.assertEquals("Item não encontrado", excecao.getMessage());
    }

    @Test
    public void busca_indisponibilidadeNaApiExterna() {
        // preparação
        var fakeStoreApiClient = Mockito.mock(FakeStoreApiClient.class);

        Mockito.doThrow(
                        new IllegalArgumentException("API EXTERNA COM PROBLEMAS!")
                )
                .when(fakeStoreApiClient)
                .pegaItem(
                        Mockito.any()
                );

        var service = new ProductsUseCaseImpl(fakeStoreApiClient);

        // execução e avaliação
        var excecao = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.busca(1L, 1L);
        });

        verify(fakeStoreApiClient, times(1)).pegaItem(Mockito.any());
        Assertions.assertEquals("Problemas com a API de ITEM", excecao.getMessage());
    }

    @ParameterizedTest
    @ValueSource(longs = {
            -1000,
            -2,
            0
    })
    public void busca_campoQuantityInvalido(final Long quantity) {
        // preparação
        var fakeStoreApiClient = Mockito.mock(FakeStoreApiClient.class);

        Mockito.when(fakeStoreApiClient.pegaItem(Mockito.any()))
                .thenReturn(
                        new ItemDTO(
                                1L,
                                "Item Teste",
                                new BigDecimal("100"),
                                "Category Teste",
                                "Description Teste",
                                "https://teste.com.br"
                        )
                );

        var service = new ProductsUseCaseImpl(fakeStoreApiClient);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            service.busca(
                    1L,
                    quantity == -1000 ? null : quantity
            );
        });

        verify(fakeStoreApiClient, times(0)).pegaItem(Mockito.any());
        Assertions.assertEquals("quantity não é um numero valido", excecao.getMessage());
    }

}
