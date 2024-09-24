package br.com.fiap.postech.adjt.cart.unitario;

import br.com.fiap.postech.adjt.cart.domain.cart.StatusEnum;
import br.com.fiap.postech.adjt.cart.domain.exception.ApiProductsException;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto.AdicionaItemRequestDTO;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto.ProductsResponseErrorDTO;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.model.CarrinhoEntity;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.model.ItensNoCarrinhoEntity;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.model.ItensNoCarrinhoId;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.repository.CarrinhoRepository;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.repository.ItensNoCarrinhoRepository;
import br.com.fiap.postech.adjt.cart.infrastructure.products.client.ProductsClient;
import br.com.fiap.postech.adjt.cart.infrastructure.products.client.response.ProductsResponseDTO;
import br.com.fiap.postech.adjt.cart.useCase.cart.impl.CartUseCaseImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CartUseCaseTest {

    @Test
    public void adiciona_carrinhoVazio_salvaNaBaseDeDados() {
        // preparação
        var productsClient = Mockito.mock(ProductsClient.class);
        var repositoryCarrinho = Mockito.mock(CarrinhoRepository.class);
        var repositoryItensNoCarrinho = Mockito.mock(ItensNoCarrinhoRepository.class);
        var mapper = Mockito.mock(ObjectMapper.class);

        Mockito.when(productsClient.busca(anyLong(), Mockito.any()))
                .thenReturn(
                        new ProductsResponseDTO(
                                1L,
                                "nome do produto",
                                new BigDecimal("100.00")
                        )
                );

        Mockito.when(repositoryCarrinho.findByUsuarioAndStatus(Mockito.any(), Mockito.any()))
                .thenReturn(
                        Optional.empty()
                );

        Mockito.when(repositoryCarrinho.save(Mockito.any()))
                .thenReturn(
                        new CarrinhoEntity(
                                1L,
                                "usuario de teste",
                                StatusEnum.ABERTO,
                                new BigDecimal("100.00"),
                                LocalDateTime.now()
                        )
                );

        Mockito.when(repositoryItensNoCarrinho.save(Mockito.any()))
                .thenReturn(
                        new ItensNoCarrinhoEntity(
                                new ItensNoCarrinhoId(1L, 123456L),
                                1L,
                                new BigDecimal("100.00")
                        )
                );

        final var service = new CartUseCaseImpl(productsClient, repositoryCarrinho, repositoryItensNoCarrinho, mapper);

        // execução
        final var adiciona = service.adiciona(
                new AdicionaItemRequestDTO(
                        "e7c5c208-c4c3-42fc-9370-3141309cb7bc",
                        1L,
                        1L
                )
        );

        // avaliação
        verify(productsClient, times(1)).busca(anyLong(), anyLong());
        verify(repositoryCarrinho, times(1)).findByUsuarioAndStatus(Mockito.any(), Mockito.any());
        verify(repositoryCarrinho, times(1)).save(Mockito.any());
        verify(repositoryItensNoCarrinho, times(1)).save(Mockito.any());
        verify(repositoryItensNoCarrinho, times(0)).findByIdIdCarrinho(Mockito.any());
        verifyNoInteractions(mapper);

        Assertions.assertEquals("Item added to cart successfully", adiciona.message());
    }

    @Test
    public void adiciona_indisponibilidadeDaApiExterna_naoSalvaNaBaseDeDados() {
        // preparação
        var productsClient = Mockito.mock(ProductsClient.class);
        var repositoryCarrinho = Mockito.mock(CarrinhoRepository.class);
        var repositoryItensNoCarrinho = Mockito.mock(ItensNoCarrinhoRepository.class);
        var mapper = Mockito.mock(ObjectMapper.class);

        Mockito.doThrow(
                        new IllegalArgumentException("API EXTERNA COM PROBLEMAS!")
                )
                .when(productsClient)
                .busca(
                        Mockito.any(),
                        Mockito.any()
                );

        Mockito.when(repositoryCarrinho.findByUsuarioAndStatus(Mockito.any(), Mockito.any()))
                .thenReturn(
                        Optional.empty()
                );

        Mockito.when(repositoryCarrinho.save(Mockito.any()))
                .thenReturn(
                        new CarrinhoEntity(
                                1L,
                                "usuario de teste",
                                StatusEnum.ABERTO,
                                new BigDecimal("100.00"),
                                LocalDateTime.now()
                        )
                );

        Mockito.when(repositoryItensNoCarrinho.save(Mockito.any()))
                .thenReturn(
                        new ItensNoCarrinhoEntity(
                                new ItensNoCarrinhoId(1L, 123456L),
                                1L,
                                new BigDecimal("100.00")
                        )
                );

        final var service = new CartUseCaseImpl(productsClient, repositoryCarrinho, repositoryItensNoCarrinho, mapper);

        // execução e avaliação
        var excecao = Assertions.assertThrows(ApiProductsException.class, () -> {
            final var adiciona = service.adiciona(
                    new AdicionaItemRequestDTO(
                            "e7c5c208-c4c3-42fc-9370-3141309cb7bc",
                            1L,
                            1L
                    )
            );
        });

        // avaliação
        verify(productsClient, times(1)).busca(anyLong(), anyLong());
        verifyNoInteractions(repositoryCarrinho);
        verifyNoInteractions(repositoryItensNoCarrinho);
        verifyNoInteractions(mapper);

        Assertions.assertEquals("Problemas com a API de PRODUCTS", excecao.getMessage());
    }

    @Test
    public void adiciona_erroNaConversaoDaMensagemDeErroDaApiExterna_naoSalvaNaBaseDeDados() throws IOException {
        // preparação
        var productsClient = Mockito.mock(ProductsClient.class);
        var repositoryCarrinho = Mockito.mock(CarrinhoRepository.class);
        var repositoryItensNoCarrinho = Mockito.mock(ItensNoCarrinhoRepository.class);
        var mapper = Mockito.mock(ObjectMapper.class);

        var request = Request.create(
                Request.HttpMethod.GET,
                "http://localhost:8080/products/1/1",
                Map.of(),
                "teste".getBytes(),
                null
        );

        Mockito.doThrow(
                new FeignException.FeignClientException(
                        400,
                        "Bad Request",
                        request,
                        "teste".getBytes(),
                        Map.of()
                    )
                )
                .when(productsClient)
                .busca(
                        Mockito.any(),
                        Mockito.any()
                );

        Mockito.doThrow(
                        new IllegalArgumentException("ERRO NO PARSE")
                )
                .when(mapper)
                .readValue(
                        any(byte[].class),
                        eq(ProductsResponseErrorDTO.class)
                );

        Mockito.when(repositoryCarrinho.findByUsuarioAndStatus(Mockito.any(), Mockito.any()))
                .thenReturn(
                        Optional.empty()
                );

        Mockito.when(repositoryCarrinho.save(Mockito.any()))
                .thenReturn(
                        new CarrinhoEntity(
                                1L,
                                "usuario de teste",
                                StatusEnum.ABERTO,
                                new BigDecimal("100.00"),
                                LocalDateTime.now()
                        )
                );

        Mockito.when(repositoryItensNoCarrinho.save(Mockito.any()))
                .thenReturn(
                        new ItensNoCarrinhoEntity(
                                new ItensNoCarrinhoId(1L, 123456L),
                                1L,
                                new BigDecimal("100.00")
                        )
                );

        final var service = new CartUseCaseImpl(productsClient, repositoryCarrinho, repositoryItensNoCarrinho, mapper);

        // execução e avaliação
        var excecao = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            final var adiciona = service.adiciona(
                    new AdicionaItemRequestDTO(
                            "e7c5c208-c4c3-42fc-9370-3141309cb7bc",
                            1L,
                            1L
                    )
            );
        });

        // avaliação
        verify(productsClient, times(1)).busca(anyLong(), anyLong());
        verify(mapper, times(1)).readValue(any(byte[].class), eq(ProductsResponseErrorDTO.class));
        verifyNoInteractions(repositoryCarrinho);
        verifyNoInteractions(repositoryItensNoCarrinho);

        Assertions.assertEquals("ERRO NO PARSE", excecao.getMessage());
    }

    @Test
    public void adiciona_itemNaoEncontrado_naoSalvaNaBaseDeDados() throws IOException {
        // preparação
        var productsClient = Mockito.mock(ProductsClient.class);
        var repositoryCarrinho = Mockito.mock(CarrinhoRepository.class);
        var repositoryItensNoCarrinho = Mockito.mock(ItensNoCarrinhoRepository.class);
        var mapper = Mockito.mock(ObjectMapper.class);

        var request = Request.create(
                Request.HttpMethod.GET,
                "http://localhost:8080/products/1/1",
                Map.of(),
                "teste".getBytes(),
                null
        );

        Mockito.doThrow(
                        new FeignException.FeignClientException(
                                400,
                                "Bad Request",
                                request,
                                "teste".getBytes(),
                                Map.of()
                        )
                )
                .when(productsClient)
                .busca(
                        Mockito.any(),
                        Mockito.any()
                );

        Mockito.when(mapper.readValue(
                any(byte[].class),
                eq(ProductsResponseErrorDTO.class))
                )
                .thenReturn(
                        new ProductsResponseErrorDTO(
                                1,
                                "Item não encontrado"
                        )
                );

        Mockito.when(repositoryCarrinho.findByUsuarioAndStatus(Mockito.any(), Mockito.any()))
                .thenReturn(
                        Optional.empty()
                );

        Mockito.when(repositoryCarrinho.save(Mockito.any()))
                .thenReturn(
                        new CarrinhoEntity(
                                1L,
                                "usuario de teste",
                                StatusEnum.ABERTO,
                                new BigDecimal("100.00"),
                                LocalDateTime.now()
                        )
                );

        Mockito.when(repositoryItensNoCarrinho.save(Mockito.any()))
                .thenReturn(
                        new ItensNoCarrinhoEntity(
                                new ItensNoCarrinhoId(1L, 123456L),
                                1L,
                                new BigDecimal("100.00")
                        )
                );

        final var service = new CartUseCaseImpl(productsClient, repositoryCarrinho, repositoryItensNoCarrinho, mapper);

        // execução e avaliação
        var excecao = Assertions.assertThrows(ApiProductsException.class, () -> {
            final var adiciona = service.adiciona(
                    new AdicionaItemRequestDTO(
                            "e7c5c208-c4c3-42fc-9370-3141309cb7bc",
                            1L,
                            1L
                    )
            );
        });

        // avaliação
        verify(productsClient, times(1)).busca(anyLong(), anyLong());
        verify(mapper, times(1)).readValue(any(byte[].class), eq(ProductsResponseErrorDTO.class));
        verifyNoInteractions(repositoryCarrinho);
        verifyNoInteractions(repositoryItensNoCarrinho);

        Assertions.assertEquals("Invalid itemId does not exist", excecao.getMessage());
    }

    @Test
    public void adiciona_quantidadeInvalida_naoSalvaNaBaseDeDados() throws IOException {
        // preparação
        var productsClient = Mockito.mock(ProductsClient.class);
        var repositoryCarrinho = Mockito.mock(CarrinhoRepository.class);
        var repositoryItensNoCarrinho = Mockito.mock(ItensNoCarrinhoRepository.class);
        var mapper = Mockito.mock(ObjectMapper.class);

        var request = Request.create(
                Request.HttpMethod.GET,
                "http://localhost:8080/products/1/1",
                Map.of(),
                "teste".getBytes(),
                null
        );

        Mockito.doThrow(
                        new FeignException.FeignClientException(
                                400,
                                "Bad Request",
                                request,
                                "teste".getBytes(),
                                Map.of()
                        )
                )
                .when(productsClient)
                .busca(
                        Mockito.any(),
                        Mockito.any()
                );

        Mockito.when(mapper.readValue(
                        any(byte[].class),
                        eq(ProductsResponseErrorDTO.class))
                )
                .thenReturn(
                        new ProductsResponseErrorDTO(
                                2,
                                "Quantidade invalida"
                        )
                );

        Mockito.when(repositoryCarrinho.findByUsuarioAndStatus(Mockito.any(), Mockito.any()))
                .thenReturn(
                        Optional.empty()
                );

        Mockito.when(repositoryCarrinho.save(Mockito.any()))
                .thenReturn(
                        new CarrinhoEntity(
                                1L,
                                "usuario de teste",
                                StatusEnum.ABERTO,
                                new BigDecimal("100.00"),
                                LocalDateTime.now()
                        )
                );

        Mockito.when(repositoryItensNoCarrinho.save(Mockito.any()))
                .thenReturn(
                        new ItensNoCarrinhoEntity(
                                new ItensNoCarrinhoId(1L, 123456L),
                                1L,
                                new BigDecimal("100.00")
                        )
                );

        final var service = new CartUseCaseImpl(productsClient, repositoryCarrinho, repositoryItensNoCarrinho, mapper);

        // execução e avaliação
        var excecao = Assertions.assertThrows(ApiProductsException.class, () -> {
            final var adiciona = service.adiciona(
                    new AdicionaItemRequestDTO(
                            "e7c5c208-c4c3-42fc-9370-3141309cb7bc",
                            1L,
                            1L
                    )
            );
        });

        // avaliação
        verify(productsClient, times(1)).busca(anyLong(), anyLong());
        verify(mapper, times(1)).readValue(any(byte[].class), eq(ProductsResponseErrorDTO.class));
        verifyNoInteractions(repositoryCarrinho);
        verifyNoInteractions(repositoryItensNoCarrinho);

        Assertions.assertEquals("Invalid itemId quantity", excecao.getMessage());
    }

    @Test
    public void adiciona_erroNaoMapeado_naoSalvaNaBaseDeDados() throws IOException {
        // preparação
        var productsClient = Mockito.mock(ProductsClient.class);
        var repositoryCarrinho = Mockito.mock(CarrinhoRepository.class);
        var repositoryItensNoCarrinho = Mockito.mock(ItensNoCarrinhoRepository.class);
        var mapper = Mockito.mock(ObjectMapper.class);

        var request = Request.create(
                Request.HttpMethod.GET,
                "http://localhost:8080/products/1/1",
                Map.of(),
                "teste".getBytes(),
                null
        );

        Mockito.doThrow(
                        new FeignException.FeignClientException(
                                400,
                                "Bad Request",
                                request,
                                "teste".getBytes(),
                                Map.of()
                        )
                )
                .when(productsClient)
                .busca(
                        Mockito.any(),
                        Mockito.any()
                );

        Mockito.when(mapper.readValue(
                        any(byte[].class),
                        eq(ProductsResponseErrorDTO.class))
                )
                .thenReturn(
                        new ProductsResponseErrorDTO(
                                500,
                                "ERRO NAO MAPEADO"
                        )
                );

        Mockito.when(repositoryCarrinho.findByUsuarioAndStatus(Mockito.any(), Mockito.any()))
                .thenReturn(
                        Optional.empty()
                );

        Mockito.when(repositoryCarrinho.save(Mockito.any()))
                .thenReturn(
                        new CarrinhoEntity(
                                1L,
                                "usuario de teste",
                                StatusEnum.ABERTO,
                                new BigDecimal("100.00"),
                                LocalDateTime.now()
                        )
                );

        Mockito.when(repositoryItensNoCarrinho.save(Mockito.any()))
                .thenReturn(
                        new ItensNoCarrinhoEntity(
                                new ItensNoCarrinhoId(1L, 123456L),
                                1L,
                                new BigDecimal("100.00")
                        )
                );

        final var service = new CartUseCaseImpl(productsClient, repositoryCarrinho, repositoryItensNoCarrinho, mapper);

        // execução e avaliação
        var excecao = Assertions.assertThrows(ApiProductsException.class, () -> {
            final var adiciona = service.adiciona(
                    new AdicionaItemRequestDTO(
                            "e7c5c208-c4c3-42fc-9370-3141309cb7bc",
                            1L,
                            1L
                    )
            );
        });

        // avaliação
        verify(productsClient, times(1)).busca(anyLong(), anyLong());
        verify(mapper, times(1)).readValue(any(byte[].class), eq(ProductsResponseErrorDTO.class));
        verifyNoInteractions(repositoryCarrinho);
        verifyNoInteractions(repositoryItensNoCarrinho);

        Assertions.assertEquals("Unmapped error PRODUCTS API", excecao.getMessage());
    }

    @Test
    public void adiciona_carrinhoExistente_salvaNaBaseDeDados() {
        // preparação
        var productsClient = Mockito.mock(ProductsClient.class);
        var repositoryCarrinho = Mockito.mock(CarrinhoRepository.class);
        var repositoryItensNoCarrinho = Mockito.mock(ItensNoCarrinhoRepository.class);
        var mapper = Mockito.mock(ObjectMapper.class);

        Mockito.when(productsClient.busca(anyLong(), Mockito.any()))
                .thenReturn(
                        new ProductsResponseDTO(
                                1L,
                                "nome do produto",
                                new BigDecimal("100.00")
                        )
                );

        Mockito.when(repositoryCarrinho.findByUsuarioAndStatus(Mockito.any(), Mockito.any()))
                .thenReturn(
                        Optional.of(
                                new CarrinhoEntity(
                                        1L,
                                        "usuario de teste",
                                        StatusEnum.ABERTO,
                                        new BigDecimal("100.00"),
                                        LocalDateTime.now()
                                )
                        )
                );

        Mockito.when(repositoryCarrinho.save(Mockito.any()))
                .thenReturn(
                        new CarrinhoEntity(
                                1L,
                                "usuario de teste",
                                StatusEnum.ABERTO,
                                new BigDecimal("100.00"),
                                LocalDateTime.now()
                        )
                );

        Mockito.when(repositoryItensNoCarrinho.save(Mockito.any()))
                .thenReturn(
                        new ItensNoCarrinhoEntity(
                                new ItensNoCarrinhoId(1L, 123456L),
                                1L,
                                new BigDecimal("100.00")
                        )
                );

        Mockito.when(repositoryItensNoCarrinho.findByIdIdCarrinho(Mockito.any()))
                .thenReturn(
                        List.of(
                                new ItensNoCarrinhoEntity(
                                        new ItensNoCarrinhoId(1L, 123456L),
                                        1L,
                                        new BigDecimal("100.00")
                                )
                        )
                );

        final var service = new CartUseCaseImpl(productsClient, repositoryCarrinho, repositoryItensNoCarrinho, mapper);

        // execução
        final var adiciona = service.adiciona(
                new AdicionaItemRequestDTO(
                        "e7c5c208-c4c3-42fc-9370-3141309cb7bc",
                        1L,
                        1L
                )
        );

        // avaliação
        verify(productsClient, times(1)).busca(anyLong(), anyLong());
        verify(repositoryCarrinho, times(1)).findByUsuarioAndStatus(Mockito.any(), Mockito.any());
        verify(repositoryCarrinho, times(1)).save(Mockito.any());
        verify(repositoryItensNoCarrinho, times(1)).save(Mockito.any());
        verify(repositoryItensNoCarrinho, times(1)).findByIdIdCarrinho(Mockito.any());
        verifyNoInteractions(mapper);

        Assertions.assertEquals("Item added to cart successfully", adiciona.message());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "-1",
            "teste",
            " ",
            "",
            "e7c5c208-c4c3-42fc-9370-3141309cb7OI"
    })
    public void adiciona_consumerIdInvalido(final String consumerId) {
        // preparação
        var productsClient = Mockito.mock(ProductsClient.class);
        var repositoryCarrinho = Mockito.mock(CarrinhoRepository.class);
        var repositoryItensNoCarrinho = Mockito.mock(ItensNoCarrinhoRepository.class);
        var mapper = Mockito.mock(ObjectMapper.class);

        Mockito.when(productsClient.busca(anyLong(), Mockito.any()))
                .thenReturn(
                        new ProductsResponseDTO(
                                1L,
                                "nome do produto",
                                new BigDecimal("100.00")
                        )
                );

        Mockito.when(repositoryCarrinho.findByUsuarioAndStatus(Mockito.any(), Mockito.any()))
                .thenReturn(
                        Optional.empty()
                );

        Mockito.when(repositoryCarrinho.save(Mockito.any()))
                .thenReturn(
                        new CarrinhoEntity(
                                1L,
                                "usuario de teste",
                                StatusEnum.ABERTO,
                                new BigDecimal("100.00"),
                                LocalDateTime.now()
                        )
                );

        Mockito.when(repositoryItensNoCarrinho.save(Mockito.any()))
                .thenReturn(
                        new ItensNoCarrinhoEntity(
                                new ItensNoCarrinhoId(1L, 123456L),
                                1L,
                                new BigDecimal("100.00")
                        )
                );

        final var service = new CartUseCaseImpl(productsClient, repositoryCarrinho, repositoryItensNoCarrinho, mapper);

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            service.adiciona(
                    new AdicionaItemRequestDTO(
                            consumerId.equals("-1") ? null : consumerId,
                            123456L,
                            1L
                    )
            );
        });

        verifyNoInteractions(productsClient);
        verifyNoInteractions(repositoryCarrinho);
        verifyNoInteractions(repositoryItensNoCarrinho);
        verifyNoInteractions(mapper);
    }

}
