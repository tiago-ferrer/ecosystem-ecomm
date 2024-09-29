package br.com.fiap.postech.adjt.checkout.unitario;

import br.com.fiap.postech.adjt.checkout.domain.checkout.StatusPagamento;
import br.com.fiap.postech.adjt.checkout.domain.exception.ApiCartException;
import br.com.fiap.postech.adjt.checkout.domain.exception.ErrorTreatedException;
import br.com.fiap.postech.adjt.checkout.infrastructure.cart.client.CartClient;
import br.com.fiap.postech.adjt.checkout.infrastructure.cart.client.request.CartRequestDTO;
import br.com.fiap.postech.adjt.checkout.infrastructure.cart.client.response.CartDetailsResponseDTO;
import br.com.fiap.postech.adjt.checkout.infrastructure.cart.client.response.CartResponseDTO;
import br.com.fiap.postech.adjt.checkout.infrastructure.cart.client.response.CartResponseErrorDTO;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto.CamposMetodoPagamentoRequestDTO;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto.MetodoPagamentoRequestDTO;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto.SolicitaPagamentoRequestDTO;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.model.ItemsOrderEntity;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.model.ItemsOrderId;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.model.OrderAsyncEntity;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.model.OrderEntity;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.repository.ItemsOrderRepository;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.repository.OrderAsyncRepository;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.repository.OrderRepository;
import br.com.fiap.postech.adjt.checkout.infrastructure.payment.client.PaymentClient;
import br.com.fiap.postech.adjt.checkout.infrastructure.payment.client.PaymentClientAsync;
import br.com.fiap.postech.adjt.checkout.infrastructure.payment.client.response.PaymentResponseDTO;
import br.com.fiap.postech.adjt.checkout.useCase.checkout.impl.CheckoutUseCaseImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CheckoutUseCaseTest {

    @Test
    public void processa_carrinhoVazio_naoSalvaNaBaseDeDados() throws JsonProcessingException {
        // preparação
        var cartClient = mock(CartClient.class);
        var webClient = mock(WebClient.class);
        var paymentClient = mock(PaymentClient.class);
        var paymentClientAsync = mock(PaymentClientAsync.class);
        var orderRepository = mock(OrderRepository.class);
        var itemsOrderRepository = mock(ItemsOrderRepository.class);
        var orderAsyncRepository = mock(OrderAsyncRepository.class);
        var objectMapper = mock(ObjectMapper.class);

        var requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        var requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        var responseSpec = mock(WebClient.ResponseSpec.class);
        when(webClient.method(eq(HttpMethod.GET)))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(eq("/")))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any(CartRequestDTO.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve())
                .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CartResponseDTO.class))
                .thenReturn(Mono.error(new WebClientResponseException(400, "Empty cart", null, new CartResponseErrorDTO("Empty cart").toString().getBytes(), null)));

        Mockito.when(objectMapper.readValue(
                        anyString(),
                        eq(CartResponseErrorDTO.class))
                )
                .thenReturn(
                        new CartResponseErrorDTO(
                                "Empty cart"
                        )
                );

        final var service = new CheckoutUseCaseImpl(cartClient, webClient, paymentClient, paymentClientAsync,
                orderRepository, itemsOrderRepository, orderAsyncRepository, objectMapper, "chaveForte");

        // execução e avaliação
        var excecao = Assertions.assertThrows(ErrorTreatedException.class, () -> {
            var processa = service.processa(new SolicitaPagamentoRequestDTO(
                    "e7c5c208-c4c3-42fc-9370-3141309cb7d0",
                    10L,
                    "BRL",
                    new MetodoPagamentoRequestDTO(
                            "br_credit_card",
                            new CamposMetodoPagamentoRequestDTO(
                                    "123456",
                                    "12",
                                    "25",
                                    "123",
                                    "Nome Teste"
                            )
                    )
            ));
        });

        verify(webClient, times(1)).method(any());
        verify(objectMapper, times(1)).readValue(anyString(), eq(CartResponseErrorDTO.class));

        verifyNoInteractions(cartClient);
        verifyNoInteractions(paymentClient);
        verifyNoInteractions(paymentClientAsync);
        verifyNoInteractions(orderRepository);
        verifyNoInteractions(itemsOrderRepository);
        verifyNoInteractions(orderAsyncRepository);
    }

    @Test
    public void processa_indisponibilidadeDaApiCart_naoSalvaNaBaseDeDados() {
        // preparação
        var cartClient = mock(CartClient.class);
        var webClient = mock(WebClient.class);
        var paymentClient = mock(PaymentClient.class);
        var paymentClientAsync = mock(PaymentClientAsync.class);
        var orderRepository = mock(OrderRepository.class);
        var itemsOrderRepository = mock(ItemsOrderRepository.class);
        var orderAsyncRepository = mock(OrderAsyncRepository.class);
        var objectMapper = mock(ObjectMapper.class);

        var requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        var requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        var responseSpec = mock(WebClient.ResponseSpec.class);
        when(webClient.method(eq(HttpMethod.GET)))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(eq("/")))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any(CartRequestDTO.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve())
                .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CartResponseDTO.class))
                .thenReturn(Mono.error(new Exception("api indisponivel")));

        final var service = new CheckoutUseCaseImpl(cartClient, webClient, paymentClient, paymentClientAsync,
                orderRepository, itemsOrderRepository, orderAsyncRepository, objectMapper, "chaveForte");

        // execução e avaliação
        var excecao = Assertions.assertThrows(ApiCartException.class, () -> {
            var processa = service.processa(new SolicitaPagamentoRequestDTO(
                    "e7c5c208-c4c3-42fc-9370-3141309cb7d0",
                    10L,
                    "BRL",
                    new MetodoPagamentoRequestDTO(
                            "br_credit_card",
                            new CamposMetodoPagamentoRequestDTO(
                                    "123456",
                                    "12",
                                    "25",
                                    "123",
                                    "Nome Teste"
                            )
                    )
            ));
        });

        verify(webClient, times(1)).method(any());

        verifyNoInteractions(cartClient);
        verifyNoInteractions(paymentClient);
        verifyNoInteractions(paymentClientAsync);
        verifyNoInteractions(orderRepository);
        verifyNoInteractions(itemsOrderRepository);
        verifyNoInteractions(orderAsyncRepository);
        verifyNoInteractions(objectMapper);
    }

    @Test
    public void processa_erroNaConversaoDaMensagemDeErroDaApiCart_naoSalvaNaBaseDeDados() throws IOException {
        // preparação
        var cartClient = mock(CartClient.class);
        var webClient = mock(WebClient.class);
        var paymentClient = mock(PaymentClient.class);
        var paymentClientAsync = mock(PaymentClientAsync.class);
        var orderRepository = mock(OrderRepository.class);
        var itemsOrderRepository = mock(ItemsOrderRepository.class);
        var orderAsyncRepository = mock(OrderAsyncRepository.class);
        var objectMapper = mock(ObjectMapper.class);

        var requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        var requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        var responseSpec = mock(WebClient.ResponseSpec.class);
        when(webClient.method(eq(HttpMethod.GET)))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(eq("/")))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any(CartRequestDTO.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve())
                .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CartResponseDTO.class))
                .thenReturn(Mono.error(new WebClientResponseException(400, "Empty cart", null, new CartResponseErrorDTO("Empty cart").toString().getBytes(), null)));

        Mockito.doThrow(
                        new RuntimeException("ERRO NO PARSE")
                )
                .when(objectMapper)
                .readValue(
                        anyString(),
                        eq(CartResponseErrorDTO.class)
                );

        final var service = new CheckoutUseCaseImpl(cartClient, webClient, paymentClient, paymentClientAsync,
                orderRepository, itemsOrderRepository, orderAsyncRepository, objectMapper, "chaveForte");

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            var processa = service.processa(new SolicitaPagamentoRequestDTO(
                    "e7c5c208-c4c3-42fc-9370-3141309cb7d0",
                    10L,
                    "BRL",
                    new MetodoPagamentoRequestDTO(
                            "br_credit_card",
                            new CamposMetodoPagamentoRequestDTO(
                                    "123456",
                                    "12",
                                    "25",
                                    "123",
                                    "Nome Teste"
                            )
                    )
            ));
        });

        verify(webClient, times(1)).method(any());
        verify(objectMapper, times(1)).readValue(anyString(), eq(CartResponseErrorDTO.class));

        verifyNoInteractions(cartClient);
        verifyNoInteractions(paymentClient);
        verifyNoInteractions(paymentClientAsync);
        verifyNoInteractions(orderRepository);
        verifyNoInteractions(itemsOrderRepository);
        verifyNoInteractions(orderAsyncRepository);
    }

    @Test
    public void processa_erroNaoEsperadoDaApiCart_naoSalvaNaBaseDeDados() {
        // preparação
        var cartClient = mock(CartClient.class);
        var webClient = mock(WebClient.class);
        var paymentClient = mock(PaymentClient.class);
        var paymentClientAsync = mock(PaymentClientAsync.class);
        var orderRepository = mock(OrderRepository.class);
        var itemsOrderRepository = mock(ItemsOrderRepository.class);
        var orderAsyncRepository = mock(OrderAsyncRepository.class);
        var objectMapper = mock(ObjectMapper.class);

        var requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        var requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        var responseSpec = mock(WebClient.ResponseSpec.class);
        when(webClient.method(eq(HttpMethod.GET)))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(eq("/")))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any(CartRequestDTO.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve())
                .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CartResponseDTO.class))
                .thenReturn(Mono.error(new WebClientResponseException(500, "Empty cart", null, new CartResponseErrorDTO("Empty cart").toString().getBytes(), null)));

        final var service = new CheckoutUseCaseImpl(cartClient, webClient, paymentClient, paymentClientAsync,
                orderRepository, itemsOrderRepository, orderAsyncRepository, objectMapper, "chaveForte");

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            var processa = service.processa(new SolicitaPagamentoRequestDTO(
                    "e7c5c208-c4c3-42fc-9370-3141309cb7d0",
                    10L,
                    "BRL",
                    new MetodoPagamentoRequestDTO(
                            "br_credit_card",
                            new CamposMetodoPagamentoRequestDTO(
                                    "123456",
                                    "12",
                                    "25",
                                    "123",
                                    "Nome Teste"
                            )
                    )
            ));
        });

        verify(webClient, times(1)).method(any());

        verifyNoInteractions(cartClient);
        verifyNoInteractions(paymentClient);
        verifyNoInteractions(paymentClientAsync);
        verifyNoInteractions(orderRepository);
        verifyNoInteractions(itemsOrderRepository);
        verifyNoInteractions(orderAsyncRepository);
        verifyNoInteractions(objectMapper);
    }

    @Test
    public void processa_erroNaoMapeadoDaApiCart_naoSalvaNaBaseDeDados() throws JsonProcessingException {
        // preparação
        var cartClient = mock(CartClient.class);
        var webClient = mock(WebClient.class);
        var paymentClient = mock(PaymentClient.class);
        var paymentClientAsync = mock(PaymentClientAsync.class);
        var orderRepository = mock(OrderRepository.class);
        var itemsOrderRepository = mock(ItemsOrderRepository.class);
        var orderAsyncRepository = mock(OrderAsyncRepository.class);
        var objectMapper = mock(ObjectMapper.class);

        var requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        var requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        var responseSpec = mock(WebClient.ResponseSpec.class);
        when(webClient.method(eq(HttpMethod.GET)))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(eq("/")))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any(CartRequestDTO.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve())
                .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CartResponseDTO.class))
                .thenReturn(Mono.error(new WebClientResponseException(400, "Novo erro", null, new CartResponseErrorDTO("Empty cart").toString().getBytes(), null)));

        Mockito.when(objectMapper.readValue(
                        anyString(),
                        eq(CartResponseErrorDTO.class))
                )
                .thenReturn(
                        new CartResponseErrorDTO(
                                "Novo erro"
                        )
                );

        final var service = new CheckoutUseCaseImpl(cartClient, webClient, paymentClient, paymentClientAsync,
                orderRepository, itemsOrderRepository, orderAsyncRepository, objectMapper, "chaveForte");

        // execução e avaliação
        var excecao = Assertions.assertThrows(ApiCartException.class, () -> {
            var processa = service.processa(new SolicitaPagamentoRequestDTO(
                    "e7c5c208-c4c3-42fc-9370-3141309cb7d0",
                    10L,
                    "BRL",
                    new MetodoPagamentoRequestDTO(
                            "br_credit_card",
                            new CamposMetodoPagamentoRequestDTO(
                                    "123456",
                                    "12",
                                    "25",
                                    "123",
                                    "Nome Teste"
                            )
                    )
            ));
        });

        verify(webClient, times(1)).method(any());
        verify(objectMapper, times(1)).readValue(anyString(), eq(CartResponseErrorDTO.class));

        verifyNoInteractions(cartClient);
        verifyNoInteractions(paymentClient);
        verifyNoInteractions(paymentClientAsync);
        verifyNoInteractions(orderRepository);
        verifyNoInteractions(itemsOrderRepository);
        verifyNoInteractions(orderAsyncRepository);
    }

    @Test
    public void processa_existePagamentoPendente_naoSalvaNaBaseDeDados() {
        // preparação
        var cartClient = mock(CartClient.class);
        var webClient = mock(WebClient.class);
        var paymentClient = mock(PaymentClient.class);
        var paymentClientAsync = mock(PaymentClientAsync.class);
        var orderRepository = mock(OrderRepository.class);
        var itemsOrderRepository = mock(ItemsOrderRepository.class);
        var orderAsyncRepository = mock(OrderAsyncRepository.class);
        var objectMapper = mock(ObjectMapper.class);

        CartResponseDTO responseDTO = new CartResponseDTO(
                List.of(new CartDetailsResponseDTO(
                        1L,
                        2L
                ))
        );

        var requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        var requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        var responseSpec = mock(WebClient.ResponseSpec.class);
        when(webClient.method(eq(HttpMethod.GET)))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(eq("/")))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any(CartRequestDTO.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve())
                .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CartResponseDTO.class))
                .thenReturn(Mono.just(responseDTO));

        Mockito.when(orderRepository.findByUsuarioAndStatus(Mockito.any(), Mockito.any()))
        .thenReturn(
                Optional.of(
                        new OrderEntity(
                                UUID.randomUUID(),
                                "usuario teste",
                                "cartao",
                                new BigDecimal("100.00"),
                                StatusPagamento.PENDING,
                                LocalDateTime.now()
                        )
                )
        );

        final var service = new CheckoutUseCaseImpl(cartClient, webClient, paymentClient, paymentClientAsync,
                orderRepository, itemsOrderRepository, orderAsyncRepository, objectMapper, "chaveForte");

        // execução e avaliação
        var excecao = Assertions.assertThrows(ErrorTreatedException.class, () -> {
            var processa = service.processa(new SolicitaPagamentoRequestDTO(
                    "e7c5c208-c4c3-42fc-9370-3141309cb7d0",
                    10L,
                    "BRL",
                    new MetodoPagamentoRequestDTO(
                            "br_credit_card",
                            new CamposMetodoPagamentoRequestDTO(
                                    "123456",
                                    "12",
                                    "25",
                                    "123",
                                    "Nome Teste"
                            )
                    )
            ));
        });

        verify(webClient, times(1)).method(any());
        verify(orderRepository, times(1)).findByUsuarioAndStatus(anyString(), any());

        verifyNoInteractions(cartClient);
        verifyNoInteractions(paymentClient);
        verifyNoInteractions(paymentClientAsync);
        verifyNoInteractions(itemsOrderRepository);
        verifyNoInteractions(orderAsyncRepository);
        verifyNoInteractions(objectMapper);
    }

    @Test
    public void processa_indisponibilidadeDaApiExternaDePagamento_naoSalvaNaBaseDeDados() {
        // preparação
        var cartClient = mock(CartClient.class);
        var webClient = mock(WebClient.class);
        var paymentClient = mock(PaymentClient.class);
        var paymentClientAsync = mock(PaymentClientAsync.class);
        var orderRepository = mock(OrderRepository.class);
        var itemsOrderRepository = mock(ItemsOrderRepository.class);
        var orderAsyncRepository = mock(OrderAsyncRepository.class);
        var objectMapper = mock(ObjectMapper.class);

        CartResponseDTO responseDTO = new CartResponseDTO(
                List.of(new CartDetailsResponseDTO(
                        1L,
                        2L
                ))
        );

        var requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        var requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        var responseSpec = mock(WebClient.ResponseSpec.class);
        when(webClient.method(eq(HttpMethod.GET)))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(eq("/")))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any(CartRequestDTO.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve())
                .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CartResponseDTO.class))
                .thenReturn(Mono.just(responseDTO));

        Mockito.when(orderRepository.findByUsuarioAndStatus(Mockito.any(), Mockito.any()))
                .thenReturn(
                        Optional.empty()
                );

        Mockito.doThrow(
                        new RuntimeException("api indisponivel")
                )
                .when(paymentClient)
                .executa(
                        Mockito.any(),
                        Mockito.any()
                );

        final var service = new CheckoutUseCaseImpl(cartClient, webClient, paymentClient, paymentClientAsync,
                orderRepository, itemsOrderRepository, orderAsyncRepository, objectMapper, "chaveForte");

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            var processa = service.processa(new SolicitaPagamentoRequestDTO(
                    "e7c5c208-c4c3-42fc-9370-3141309cb7d0",
                    10L,
                    "BRL",
                    new MetodoPagamentoRequestDTO(
                            "br_credit_card",
                            new CamposMetodoPagamentoRequestDTO(
                                    "123456",
                                    "12",
                                    "25",
                                    "123",
                                    "Nome Teste"
                            )
                    )
            ));
        });

        verify(webClient, times(1)).method(any());
        verify(orderRepository, times(1)).findByUsuarioAndStatus(anyString(), any());
        verify(paymentClient, times(1)).executa(any(), any());

        verifyNoInteractions(cartClient);
        verifyNoInteractions(paymentClientAsync);
        verifyNoInteractions(itemsOrderRepository);
        verifyNoInteractions(orderAsyncRepository);
        verifyNoInteractions(objectMapper);
    }

    @Test
    public void processa_timeoutDe300msDaApiExternaDePagamento_salvaNaBaseDeDados() {
        // preparação
        var cartClient = mock(CartClient.class);
        var webClient = mock(WebClient.class);
        var paymentClient = mock(PaymentClient.class);
        var paymentClientAsync = mock(PaymentClientAsync.class);
        var orderRepository = mock(OrderRepository.class);
        var itemsOrderRepository = mock(ItemsOrderRepository.class);
        var orderAsyncRepository = mock(OrderAsyncRepository.class);
        var objectMapper = mock(ObjectMapper.class);

        CartResponseDTO responseDTO = new CartResponseDTO(
                List.of(new CartDetailsResponseDTO(
                        1L,
                        2L
                ))
        );

        var requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        var requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        var responseSpec = mock(WebClient.ResponseSpec.class);
        when(webClient.method(eq(HttpMethod.GET)))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(eq("/")))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any(CartRequestDTO.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve())
                .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CartResponseDTO.class))
                .thenReturn(Mono.just(responseDTO));

        Mockito.when(orderRepository.findByUsuarioAndStatus(Mockito.any(), Mockito.any()))
                .thenReturn(
                        Optional.empty()
                );

        Mockito.doThrow(
                        new RuntimeException(new SocketTimeoutException("timeout"))
                )
                .when(paymentClient)
                .executa(
                        Mockito.any(),
                        Mockito.any()
                );

        Mockito.when(orderAsyncRepository.save(Mockito.any()))
                .thenReturn(
                        null
                );

        final var orderId = UUID.randomUUID();
        Mockito.when(orderRepository.save(Mockito.any()))
                .thenReturn(
                        new OrderEntity(
                                orderId,
                                "usuario teste",
                                "cartao",
                                new BigDecimal("100.00"),
                                StatusPagamento.PENDING,
                                LocalDateTime.now()
                        )
                );

        Mockito.when(itemsOrderRepository.save(Mockito.any()))
                .thenReturn(
                        null
                );

        Mockito.when(cartClient.deletaOCarrinho(Mockito.any()))
                .thenReturn(
                        null
                );

        final var service = new CheckoutUseCaseImpl(cartClient, webClient, paymentClient, paymentClientAsync,
                orderRepository, itemsOrderRepository, orderAsyncRepository, objectMapper, "chaveForte");

        // execução
        var processa = service.processa(new SolicitaPagamentoRequestDTO(
                "e7c5c208-c4c3-42fc-9370-3141309cb7d0",
                10L,
                "BRL",
                new MetodoPagamentoRequestDTO(
                        "br_credit_card",
                        new CamposMetodoPagamentoRequestDTO(
                                "123456",
                                "12",
                                "25",
                                "123",
                                "Nome Teste"
                        )
                )
        ));

        // avaliação
        Assertions.assertEquals(orderId.toString(), processa.orderId());
        Assertions.assertEquals(StatusPagamento.PENDING.name(), processa.status());

        verify(webClient, times(1)).method(any());
        verify(orderRepository, times(1)).findByUsuarioAndStatus(anyString(), any());
        verify(paymentClient, times(1)).executa(any(), any());
        verify(orderAsyncRepository, times(1)).save(any());
        verify(orderRepository, times(1)).save(any());
        verify(itemsOrderRepository, times(1)).saveAll(any());
        verify(itemsOrderRepository, times(0)).save(any());
        verify(cartClient, times(1)).deletaOCarrinho(any());

        verifyNoInteractions(paymentClientAsync);
        verifyNoInteractions(objectMapper);
    }

    @Test
    public void processa_apiExternaDePagamentoRespondeNormal_salvaNaBaseDeDados() {
        // preparação
        var cartClient = mock(CartClient.class);
        var webClient = mock(WebClient.class);
        var paymentClient = mock(PaymentClient.class);
        var paymentClientAsync = mock(PaymentClientAsync.class);
        var orderRepository = mock(OrderRepository.class);
        var itemsOrderRepository = mock(ItemsOrderRepository.class);
        var orderAsyncRepository = mock(OrderAsyncRepository.class);
        var objectMapper = mock(ObjectMapper.class);

        CartResponseDTO responseDTO = new CartResponseDTO(
                List.of(new CartDetailsResponseDTO(
                        1L,
                        2L
                ))
        );

        var requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        var requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        var responseSpec = mock(WebClient.ResponseSpec.class);
        when(webClient.method(eq(HttpMethod.GET)))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(eq("/")))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any(CartRequestDTO.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve())
                .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CartResponseDTO.class))
                .thenReturn(Mono.just(responseDTO));

        Mockito.when(orderRepository.findByUsuarioAndStatus(Mockito.any(), Mockito.any()))
                .thenReturn(
                        Optional.empty()
                );

        final var orderId = UUID.randomUUID();
        Mockito.when(paymentClient.executa(any(), any()))
                .thenReturn(
                        new PaymentResponseDTO(
                                orderId.toString(),
                                StatusPagamento.APPROVED.name()
                        )
                );

        Mockito.when(orderAsyncRepository.save(Mockito.any()))
                .thenReturn(
                        null
                );

        Mockito.when(orderRepository.save(Mockito.any()))
                .thenReturn(
                        new OrderEntity(
                                orderId,
                                "usuario teste",
                                "cartao",
                                new BigDecimal("100.00"),
                                StatusPagamento.APPROVED,
                                LocalDateTime.now()
                        )
                );

        Mockito.when(itemsOrderRepository.save(Mockito.any()))
                .thenReturn(
                        null
                );

        Mockito.when(cartClient.deletaOCarrinho(Mockito.any()))
                .thenReturn(
                        null
                );

        final var service = new CheckoutUseCaseImpl(cartClient, webClient, paymentClient, paymentClientAsync,
                orderRepository, itemsOrderRepository, orderAsyncRepository, objectMapper, "chaveForte");

        // execução
        var processa = service.processa(new SolicitaPagamentoRequestDTO(
                "e7c5c208-c4c3-42fc-9370-3141309cb7d0",
                10L,
                "BRL",
                new MetodoPagamentoRequestDTO(
                        "br_credit_card",
                        new CamposMetodoPagamentoRequestDTO(
                                "123456",
                                "12",
                                "25",
                                "123",
                                "Nome Teste"
                        )
                )
        ));

        // avaliação
        Assertions.assertEquals(orderId.toString(), processa.orderId());
        Assertions.assertEquals(StatusPagamento.APPROVED.name(), processa.status());

        verify(webClient, times(1)).method(any());
        verify(orderRepository, times(1)).findByUsuarioAndStatus(anyString(), any());
        verify(paymentClient, times(1)).executa(any(), any());
        verify(orderRepository, times(1)).save(any());
        verify(itemsOrderRepository, times(1)).saveAll(any());
        verify(itemsOrderRepository, times(0)).save(any());
        verify(cartClient, times(1)).deletaOCarrinho(any());

        verifyNoInteractions(paymentClientAsync);
        verifyNoInteractions(orderAsyncRepository);
        verifyNoInteractions(objectMapper);
    }

    @Test
    public void processaPendentes_apiExternaDePagamentoRespondeNormal_salvaNaBaseDeDados() {
        // preparação
        var cartClient = mock(CartClient.class);
        var webClient = mock(WebClient.class);
        var paymentClient = mock(PaymentClient.class);
        var paymentClientAsync = mock(PaymentClientAsync.class);
        var orderRepository = mock(OrderRepository.class);
        var itemsOrderRepository = mock(ItemsOrderRepository.class);
        var orderAsyncRepository = mock(OrderAsyncRepository.class);
        var objectMapper = mock(ObjectMapper.class);

        final var orderId = UUID.randomUUID();
        final var orderId2 = UUID.randomUUID();
        Mockito.when(orderAsyncRepository.findAll())
                .thenReturn(
                        List.of(
                                new OrderAsyncEntity(
                                        orderId,
                                        orderId,
                                        "e7c5c208-c4c3-42fc-9370-3141309cb7bc",
                                        10L,
                                        "BRL",
                                        "br_credit_card",
                                        "123456",
                                        "12",
                                        "25",
                                        "123",
                                        "Nome Teste",
                                        LocalDateTime.now()
                                ),
                                new OrderAsyncEntity(
                                        orderId2,
                                        orderId2,
                                        "e7c5c208-c4c3-42fc-9370-3141309cb7bc",
                                        10L,
                                        "BRL",
                                        "br_credit_card",
                                        "123456",
                                        "12",
                                        "25",
                                        "123",
                                        "Nome Teste",
                                        LocalDateTime.now()
                                )
                        )
                );

        Mockito.when(paymentClientAsync.executa(any(), any()))
                .thenReturn(
                        new PaymentResponseDTO(
                                orderId.toString(),
                                StatusPagamento.APPROVED.name()
                        ),
                        new PaymentResponseDTO(
                                orderId2.toString(),
                                StatusPagamento.DECLINED.name()
                        )
                );

        doNothing().when(orderAsyncRepository).delete(Mockito.any());

        Mockito.when(orderRepository.save(Mockito.any()))
                .thenReturn(
                        new OrderEntity(
                                orderId,
                                "usuario teste",
                                "cartao",
                                new BigDecimal("100.00"),
                                StatusPagamento.APPROVED,
                                LocalDateTime.now()
                        )
                );

        final var service = new CheckoutUseCaseImpl(cartClient, webClient, paymentClient, paymentClientAsync,
                orderRepository, itemsOrderRepository, orderAsyncRepository, objectMapper, "chaveForte");

        // execução
        service.processaPendentes();

        verify(orderRepository, times(0)).findByUsuarioAndStatus(anyString(), any());
        verify(paymentClientAsync, times(2)).executa(any(), any());
        verify(orderRepository, times(2)).save(any());
        verify(orderAsyncRepository, times(1)).findAll();
        verify(orderAsyncRepository, times(2)).delete(any());
        verify(orderAsyncRepository, times(0)).save(any());

        verifyNoInteractions(webClient);
        verifyNoInteractions(paymentClient);
        verifyNoInteractions(itemsOrderRepository);
        verifyNoInteractions(objectMapper);
        verifyNoInteractions(cartClient);
    }

    @Test
    public void processaPendentes_indisponibilidadeApiExternaDePagamento_naoSalvaNaBaseDeDados() {
        // preparação
        var cartClient = mock(CartClient.class);
        var webClient = mock(WebClient.class);
        var paymentClient = mock(PaymentClient.class);
        var paymentClientAsync = mock(PaymentClientAsync.class);
        var orderRepository = mock(OrderRepository.class);
        var itemsOrderRepository = mock(ItemsOrderRepository.class);
        var orderAsyncRepository = mock(OrderAsyncRepository.class);
        var objectMapper = mock(ObjectMapper.class);

        final var orderId = UUID.randomUUID();
        final var orderId2 = UUID.randomUUID();
        Mockito.when(orderAsyncRepository.findAll())
                .thenReturn(
                        List.of(
                                new OrderAsyncEntity(
                                        orderId,
                                        orderId,
                                        "e7c5c208-c4c3-42fc-9370-3141309cb7bc",
                                        10L,
                                        "BRL",
                                        "br_credit_card",
                                        "123456",
                                        "12",
                                        "25",
                                        "123",
                                        "Nome Teste",
                                        LocalDateTime.now()
                                ),
                                new OrderAsyncEntity(
                                        orderId2,
                                        orderId2,
                                        "e7c5c208-c4c3-42fc-9370-3141309cb7bc",
                                        10L,
                                        "BRL",
                                        "br_credit_card",
                                        "123456",
                                        "12",
                                        "25",
                                        "123",
                                        "Nome Teste",
                                        LocalDateTime.now()
                                )
                        )
                );

        Mockito.doThrow(
                        new RuntimeException("api indisponivel")
                )
                .when(paymentClient)
                .executa(
                        Mockito.any(),
                        Mockito.any()
                );

        doNothing().when(orderAsyncRepository).delete(Mockito.any());

        Mockito.when(orderRepository.save(Mockito.any()))
                .thenReturn(
                        new OrderEntity(
                                orderId,
                                "usuario teste",
                                "cartao",
                                new BigDecimal("100.00"),
                                StatusPagamento.APPROVED,
                                LocalDateTime.now()
                        )
                );

        final var service = new CheckoutUseCaseImpl(cartClient, webClient, paymentClient, paymentClientAsync,
                orderRepository, itemsOrderRepository, orderAsyncRepository, objectMapper, "chaveForte");

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, service::processaPendentes);

        verify(paymentClientAsync, times(1)).executa(any(), any());
        verify(orderAsyncRepository, times(1)).findAll();
        verify(orderAsyncRepository, times(0)).delete(any());
        verify(orderAsyncRepository, times(0)).save(any());

        verifyNoInteractions(webClient);
        verifyNoInteractions(orderRepository);
        verifyNoInteractions(paymentClient);
        verifyNoInteractions(itemsOrderRepository);
        verifyNoInteractions(objectMapper);
        verifyNoInteractions(cartClient);
    }

    @Test
    public void processaPendentes_semPagamentosPendentes_naoSalvaNaBaseDeDados() {
        // preparação
        var cartClient = mock(CartClient.class);
        var webClient = mock(WebClient.class);
        var paymentClient = mock(PaymentClient.class);
        var paymentClientAsync = mock(PaymentClientAsync.class);
        var orderRepository = mock(OrderRepository.class);
        var itemsOrderRepository = mock(ItemsOrderRepository.class);
        var orderAsyncRepository = mock(OrderAsyncRepository.class);
        var objectMapper = mock(ObjectMapper.class);

        final var orderId = UUID.randomUUID();
        final var orderId2 = UUID.randomUUID();
        Mockito.when(orderAsyncRepository.findAll())
                .thenReturn(
                        List.of()
                );

        Mockito.when(paymentClientAsync.executa(any(), any()))
                .thenReturn(
                        new PaymentResponseDTO(
                                orderId.toString(),
                                StatusPagamento.APPROVED.name()
                        ),
                        new PaymentResponseDTO(
                                orderId2.toString(),
                                StatusPagamento.DECLINED.name()
                        )
                );

        doNothing().when(orderAsyncRepository).delete(Mockito.any());

        Mockito.when(orderRepository.save(Mockito.any()))
                .thenReturn(
                        new OrderEntity(
                                orderId,
                                "usuario teste",
                                "cartao",
                                new BigDecimal("100.00"),
                                StatusPagamento.APPROVED,
                                LocalDateTime.now()
                        )
                );

        final var service = new CheckoutUseCaseImpl(cartClient, webClient, paymentClient, paymentClientAsync,
                orderRepository, itemsOrderRepository, orderAsyncRepository, objectMapper, "chaveForte");

        // execução
        service.processaPendentes();

        verify(orderAsyncRepository, times(1)).findAll();
        verify(orderAsyncRepository, times(0)).delete(any());
        verify(orderAsyncRepository, times(0)).save(any());

        verifyNoInteractions(webClient);
        verifyNoInteractions(orderRepository);
        verifyNoInteractions(paymentClientAsync);
        verifyNoInteractions(paymentClient);
        verifyNoInteractions(itemsOrderRepository);
        verifyNoInteractions(objectMapper);
        verifyNoInteractions(cartClient);
    }

    @Test
    public void busca_sucesso_buscaNaBaseDeDados() {
        // preparação
        var cartClient = mock(CartClient.class);
        var webClient = mock(WebClient.class);
        var paymentClient = mock(PaymentClient.class);
        var paymentClientAsync = mock(PaymentClientAsync.class);
        var orderRepository = mock(OrderRepository.class);
        var itemsOrderRepository = mock(ItemsOrderRepository.class);
        var orderAsyncRepository = mock(OrderAsyncRepository.class);
        var objectMapper = mock(ObjectMapper.class);

        final var orderId = UUID.randomUUID();
        final var orderId2 = UUID.randomUUID();
        Mockito.when(orderRepository.findByUsuario(Mockito.any()))
                .thenReturn(
                        List.of(
                                new OrderEntity(
                                        orderId,
                                        "e7c5c208-c4c3-42fc-9370-3141309cb7d0",
                                        "cartao",
                                        new BigDecimal("100.00"),
                                        StatusPagamento.APPROVED,
                                        LocalDateTime.now()
                                ),
                                new OrderEntity(
                                        orderId2,
                                        "e7c5c208-c4c3-42fc-9370-3141309cb7d0",
                                        "pix",
                                        new BigDecimal("200.00"),
                                        StatusPagamento.DECLINED,
                                        LocalDateTime.now()
                                )
                        )
                );

        Mockito.when(itemsOrderRepository.findByIdIdOrder(Mockito.any()))
                .thenReturn(
                        List.of(
                                new ItemsOrderEntity(
                                        new ItemsOrderId(orderId, 1L),
                                        3L
                                ),
                                new ItemsOrderEntity(
                                        new ItemsOrderId(orderId, 2L),
                                        4L
                                )
                        ),
                        List.of(
                                new ItemsOrderEntity(
                                        new ItemsOrderId(orderId2, 1L),
                                        3L
                                ),
                                new ItemsOrderEntity(
                                        new ItemsOrderId(orderId2, 2L),
                                        4L
                                )
                        )
                );

        final var service = new CheckoutUseCaseImpl(cartClient, webClient, paymentClient, paymentClientAsync,
                orderRepository, itemsOrderRepository, orderAsyncRepository, objectMapper, "chaveForte");

        // execução
        var busca = service.busca("e7c5c208-c4c3-42fc-9370-3141309cb7d0");

        // avaliação
        Assertions.assertEquals(2, busca.orders().size());

        verify(orderRepository, times(0)).findByUsuarioAndStatus(anyString(), any());
        verify(orderRepository, times(0)).save(any());
        verify(orderRepository, times(1)).findByUsuario(any());
        verify(itemsOrderRepository, times(0)).saveAll(any());
        verify(itemsOrderRepository, times(0)).save(any());
        verify(itemsOrderRepository, times(2)).findByIdIdOrder(any());

        verifyNoInteractions(cartClient);
        verifyNoInteractions(paymentClientAsync);
        verifyNoInteractions(paymentClient);
        verifyNoInteractions(webClient);
        verifyNoInteractions(orderAsyncRepository);
        verifyNoInteractions(objectMapper);
    }

    @Test
    public void busca_semPagamento_buscaNaBaseDeDados() {
        // preparação
        var cartClient = mock(CartClient.class);
        var webClient = mock(WebClient.class);
        var paymentClient = mock(PaymentClient.class);
        var paymentClientAsync = mock(PaymentClientAsync.class);
        var orderRepository = mock(OrderRepository.class);
        var itemsOrderRepository = mock(ItemsOrderRepository.class);
        var orderAsyncRepository = mock(OrderAsyncRepository.class);
        var objectMapper = mock(ObjectMapper.class);

        final var orderId = UUID.randomUUID();
        final var orderId2 = UUID.randomUUID();
        Mockito.when(orderRepository.findByUsuario(Mockito.any()))
                .thenReturn(
                        List.of()
                );

        Mockito.when(itemsOrderRepository.findByIdIdOrder(Mockito.any()))
                .thenReturn(
                        List.of(
                                new ItemsOrderEntity(
                                        new ItemsOrderId(orderId, 1L),
                                        3L
                                ),
                                new ItemsOrderEntity(
                                        new ItemsOrderId(orderId, 2L),
                                        4L
                                )
                        ),
                        List.of(
                                new ItemsOrderEntity(
                                        new ItemsOrderId(orderId2, 1L),
                                        3L
                                ),
                                new ItemsOrderEntity(
                                        new ItemsOrderId(orderId2, 2L),
                                        4L
                                )
                        )
                );

        final var service = new CheckoutUseCaseImpl(cartClient, webClient, paymentClient, paymentClientAsync,
                orderRepository, itemsOrderRepository, orderAsyncRepository, objectMapper, "chaveForte");

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            var busca = service.busca("e7c5c208-c4c3-42fc-9370-3141309cb7d0");
        });

        verify(orderRepository, times(0)).findByUsuarioAndStatus(anyString(), any());
        verify(orderRepository, times(0)).save(any());
        verify(orderRepository, times(1)).findByUsuario(any());

        verifyNoInteractions(cartClient);
        verifyNoInteractions(itemsOrderRepository);
        verifyNoInteractions(paymentClientAsync);
        verifyNoInteractions(paymentClient);
        verifyNoInteractions(webClient);
        verifyNoInteractions(orderAsyncRepository);
        verifyNoInteractions(objectMapper);
    }

    @Test
    public void buscaPorOrderId_sucesso_buscaNaBaseDeDados() {
        // preparação
        var cartClient = mock(CartClient.class);
        var webClient = mock(WebClient.class);
        var paymentClient = mock(PaymentClient.class);
        var paymentClientAsync = mock(PaymentClientAsync.class);
        var orderRepository = mock(OrderRepository.class);
        var itemsOrderRepository = mock(ItemsOrderRepository.class);
        var orderAsyncRepository = mock(OrderAsyncRepository.class);
        var objectMapper = mock(ObjectMapper.class);

        final var orderId = UUID.randomUUID();
        final var orderId2 = UUID.randomUUID();
        Mockito.when(orderRepository.findById(Mockito.any()))
                .thenReturn(
                            Optional.of(new OrderEntity(
                                    orderId,
                                    "e7c5c208-c4c3-42fc-9370-3141309cb7d0",
                                    "cartao",
                                    new BigDecimal("100.00"),
                                    StatusPagamento.APPROVED,
                                    LocalDateTime.now()
                            ))
                );

        Mockito.when(itemsOrderRepository.findByIdIdOrder(Mockito.any()))
                .thenReturn(
                        List.of(
                                new ItemsOrderEntity(
                                        new ItemsOrderId(orderId, 1L),
                                        3L
                                ),
                                new ItemsOrderEntity(
                                        new ItemsOrderId(orderId, 2L),
                                        4L
                                )
                        ),
                        List.of(
                                new ItemsOrderEntity(
                                        new ItemsOrderId(orderId2, 1L),
                                        3L
                                ),
                                new ItemsOrderEntity(
                                        new ItemsOrderId(orderId2, 2L),
                                        4L
                                )
                        )
                );

        final var service = new CheckoutUseCaseImpl(cartClient, webClient, paymentClient, paymentClientAsync,
                orderRepository, itemsOrderRepository, orderAsyncRepository, objectMapper, "chaveForte");

        // execução
        var busca = service.buscaPorOrderId(orderId.toString());

        // avaliação
        Assertions.assertEquals(orderId.toString(), busca.orderId());
        Assertions.assertEquals(2, busca.items().size());
        Assertions.assertEquals("cartao", busca.paymentType());
        Assertions.assertEquals(new BigDecimal("100.00"), busca.value());
        Assertions.assertEquals(StatusPagamento.APPROVED.name(), busca.paymentStatus());

        verify(orderRepository, times(0)).findByUsuarioAndStatus(anyString(), any());
        verify(orderRepository, times(0)).save(any());
        verify(orderRepository, times(0)).findByUsuario(any());
        verify(orderRepository, times(1)).findById(any());
        verify(itemsOrderRepository, times(0)).saveAll(any());
        verify(itemsOrderRepository, times(0)).save(any());
        verify(itemsOrderRepository, times(1)).findByIdIdOrder(any());

        verifyNoInteractions(cartClient);
        verifyNoInteractions(paymentClientAsync);
        verifyNoInteractions(paymentClient);
        verifyNoInteractions(webClient);
        verifyNoInteractions(orderAsyncRepository);
        verifyNoInteractions(objectMapper);
    }

    @Test
    public void buscaPorOrderId_semPagamento_buscaNaBaseDeDados() {
        // preparação
        var cartClient = mock(CartClient.class);
        var webClient = mock(WebClient.class);
        var paymentClient = mock(PaymentClient.class);
        var paymentClientAsync = mock(PaymentClientAsync.class);
        var orderRepository = mock(OrderRepository.class);
        var itemsOrderRepository = mock(ItemsOrderRepository.class);
        var orderAsyncRepository = mock(OrderAsyncRepository.class);
        var objectMapper = mock(ObjectMapper.class);

        final var orderId = UUID.randomUUID();
        final var orderId2 = UUID.randomUUID();
        Mockito.when(orderRepository.findById(Mockito.any()))
                .thenReturn(
                        Optional.empty()
                );

        Mockito.when(itemsOrderRepository.findByIdIdOrder(Mockito.any()))
                .thenReturn(
                        List.of(
                                new ItemsOrderEntity(
                                        new ItemsOrderId(orderId, 1L),
                                        3L
                                ),
                                new ItemsOrderEntity(
                                        new ItemsOrderId(orderId, 2L),
                                        4L
                                )
                        ),
                        List.of(
                                new ItemsOrderEntity(
                                        new ItemsOrderId(orderId2, 1L),
                                        3L
                                ),
                                new ItemsOrderEntity(
                                        new ItemsOrderId(orderId2, 2L),
                                        4L
                                )
                        )
                );

        final var service = new CheckoutUseCaseImpl(cartClient, webClient, paymentClient, paymentClientAsync,
                orderRepository, itemsOrderRepository, orderAsyncRepository, objectMapper, "chaveForte");

        // execução e avaliação
        var excecao = Assertions.assertThrows(RuntimeException.class, () -> {
            var busca = service.buscaPorOrderId(orderId2.toString());
        });

        verify(orderRepository, times(0)).findByUsuarioAndStatus(anyString(), any());
        verify(orderRepository, times(0)).save(any());
        verify(orderRepository, times(0)).findByUsuario(any());
        verify(orderRepository, times(1)).findById(any());

        verifyNoInteractions(cartClient);
        verifyNoInteractions(itemsOrderRepository);
        verifyNoInteractions(paymentClientAsync);
        verifyNoInteractions(paymentClient);
        verifyNoInteractions(webClient);
        verifyNoInteractions(orderAsyncRepository);
        verifyNoInteractions(objectMapper);
    }

    @ParameterizedTest
    @MethodSource("requestValidandoCampos")
    public void processa_camposInvalidos(final Long amount,
                                         final String currency,
                                         final String methodPaymentType,
                                         final String number,
                                         final String expirationMonth,
                                         final String expirationYear,
                                         final String cvv,
                                         final String name) {
        // preparação
        var cartClient = mock(CartClient.class);
        var webClient = mock(WebClient.class);
        var paymentClient = mock(PaymentClient.class);
        var paymentClientAsync = mock(PaymentClientAsync.class);
        var orderRepository = mock(OrderRepository.class);
        var itemsOrderRepository = mock(ItemsOrderRepository.class);
        var orderAsyncRepository = mock(OrderAsyncRepository.class);
        var objectMapper = mock(ObjectMapper.class);

        WebClient.RequestBodyUriSpec mock1 = mock(WebClient.RequestBodyUriSpec.class);
        when(webClient.method(HttpMethod.GET))
                .thenReturn(mock1);

        final var service = new CheckoutUseCaseImpl(cartClient, webClient, paymentClient, paymentClientAsync,
                orderRepository, itemsOrderRepository, orderAsyncRepository, objectMapper, "chaveForte");

        // execução e avaliação
        var excecao = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            var processa = service.processa(new SolicitaPagamentoRequestDTO(
                    "e7c5c208-c4c3-42fc-9370-3141309cb7d0",
                    amount == -1 ? null : amount,
                    currency.equals("-1") ? null : currency,
                    new MetodoPagamentoRequestDTO(
                            methodPaymentType.equals("-1") ? null : methodPaymentType,
                            new CamposMetodoPagamentoRequestDTO(
                                    number.equals("-1") ? null : number,
                                    expirationMonth.equals("-1") ? null : expirationMonth,
                                    expirationYear.equals("-1") ? null : expirationYear,
                                    cvv.equals("-1") ? null : cvv,
                                    name.equals("-1") ? null : name
                            )
                    )
            ));
        });

        verifyNoInteractions(cartClient);
        verifyNoInteractions(webClient);
        verifyNoInteractions(paymentClient);
        verifyNoInteractions(paymentClientAsync);
        verifyNoInteractions(orderRepository);
        verifyNoInteractions(itemsOrderRepository);
        verifyNoInteractions(orderAsyncRepository);
        verifyNoInteractions(objectMapper);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "-1",
            "teste",
            " ",
            "",
            "e7c5c208-c4c3-42fc-9370-3141309cb7OI"
    })
    public void processa_consumerIdInvalido(final String consumerId) throws JsonProcessingException {
        // preparação
        var cartClient = mock(CartClient.class);
        var webClient = mock(WebClient.class);
        var paymentClient = mock(PaymentClient.class);
        var paymentClientAsync = mock(PaymentClientAsync.class);
        var orderRepository = mock(OrderRepository.class);
        var itemsOrderRepository = mock(ItemsOrderRepository.class);
        var orderAsyncRepository = mock(OrderAsyncRepository.class);
        var objectMapper = mock(ObjectMapper.class);

        var requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        var requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        var responseSpec = mock(WebClient.ResponseSpec.class);
        when(webClient.method(eq(HttpMethod.GET)))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(eq("/")))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any(CartRequestDTO.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve())
                .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CartResponseDTO.class))
                .thenReturn(Mono.error(new WebClientResponseException(400, "Invalid consumerId format", null, new CartResponseErrorDTO("Invalid consumerId format").toString().getBytes(), null)));

        Mockito.when(objectMapper.readValue(
                        anyString(),
                        eq(CartResponseErrorDTO.class))
                )
                .thenReturn(
                        new CartResponseErrorDTO(
                                "Invalid consumerId format"
                        )
                );

        final var service = new CheckoutUseCaseImpl(cartClient, webClient, paymentClient, paymentClientAsync,
                orderRepository, itemsOrderRepository, orderAsyncRepository, objectMapper, "chaveForte");

        // execução e avaliação
        var excecao = Assertions.assertThrows(ErrorTreatedException.class, () -> {
            var processa = service.processa(new SolicitaPagamentoRequestDTO(
                    consumerId.equals("-1") ? null : consumerId,
                    10L,
                    "BRL",
                    new MetodoPagamentoRequestDTO(
                            "br_credit_card",
                            new CamposMetodoPagamentoRequestDTO(
                                    "123456",
                                    "12",
                                    "25",
                                    "123",
                                    "Nome Teste"
                            )
                    )
            ));
        });

        verify(webClient, times(1)).method(any());
        verify(objectMapper, times(1)).readValue(anyString(), eq(CartResponseErrorDTO.class));

        verifyNoInteractions(cartClient);
        verifyNoInteractions(paymentClient);
        verifyNoInteractions(paymentClientAsync);
        verifyNoInteractions(orderRepository);
        verifyNoInteractions(itemsOrderRepository);
        verifyNoInteractions(orderAsyncRepository);
    }

    private static Stream<Arguments> requestValidandoCampos() {
        return Stream.of(
                Arguments.of(-1L, "BRL", "br_credit_card", "123456", "12", "25", "123", "Nome Teste"),
                Arguments.of(0L, "BRL", "br_credit_card", "123456", "12", "25", "123", "Nome Teste"),
                Arguments.of(-2L, "BRL", "br_credit_card", "123456", "12", "25", "123", "Nome Teste"),
                Arguments.of(10L, "-1", "br_credit_card", "123456", "12", "25", "123", "Nome Teste"),
                Arguments.of(10L, " ", "br_credit_card", "123456", "12", "25", "123", "Nome Teste"),
                Arguments.of(10L, "", "br_credit_card", "123456", "12", "25", "123", "Nome Teste"),
                Arguments.of(10L, "BRL", "-1", "123456", "12", "25", "123", "Nome Teste"),
                Arguments.of(10L, "BRL", " ", "123456", "12", "25", "123", "Nome Teste"),
                Arguments.of(10L, "BRL", "", "123456", "12", "25", "123", "Nome Teste"),
                Arguments.of(10L, "BRL", "br_credit_card", "-1", "12", "25", "123", "Nome Teste"),
                Arguments.of(10L, "BRL", "br_credit_card", " ", "12", "25", "123", "Nome Teste"),
                Arguments.of(10L, "BRL", "br_credit_card", "", "12", "25", "123", "Nome Teste"),
                Arguments.of(10L, "BRL", "br_credit_card", "123456", "-1", "25", "123", "Nome Teste"),
                Arguments.of(10L, "BRL", "br_credit_card", "123456", " ", "25", "123", "Nome Teste"),
                Arguments.of(10L, "BRL", "br_credit_card", "123456", "", "25", "123", "Nome Teste"),
                Arguments.of(10L, "BRL", "br_credit_card", "123456", "a", "25", "123", "Nome Teste"),
                Arguments.of(10L, "BRL", "br_credit_card", "123456", "abc", "25", "123", "Nome Teste"),
                Arguments.of(10L, "BRL", "br_credit_card", "123456", "12", "-1", "123", "Nome Teste"),
                Arguments.of(10L, "BRL", "br_credit_card", "123456", "12", " ", "123", "Nome Teste"),
                Arguments.of(10L, "BRL", "br_credit_card", "123456", "12", "", "123", "Nome Teste"),
                Arguments.of(10L, "BRL", "br_credit_card", "123456", "12", "a", "123", "Nome Teste"),
                Arguments.of(10L, "BRL", "br_credit_card", "123456", "12", "abc", "123", "Nome Teste"),
                Arguments.of(10L, "BRL", "br_credit_card", "123456", "12", "25", "-1", "Nome Teste"),
                Arguments.of(10L, "BRL", "br_credit_card", "123456", "12", "25", " ", "Nome Teste"),
                Arguments.of(10L, "BRL", "br_credit_card", "123456", "12", "25", "", "Nome Teste"),
                Arguments.of(10L, "BRL", "br_credit_card", "123456", "12", "25", "a", "Nome Teste"),
                Arguments.of(10L, "BRL", "br_credit_card", "123456", "12", "25", "abcd", "Nome Teste"),
                Arguments.of(10L, "BRL", "br_credit_card", "123456", "12", "25", "123", "-1"),
                Arguments.of(10L, "BRL", "br_credit_card", "123456", "12", "25", "123", " "),
                Arguments.of(10L, "BRL", "br_credit_card", "123456", "12", "25", "123", "")
        );
    }

}
