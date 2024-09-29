package br.com.fiap.postech.adjt.checkout.useCase.checkout.impl;

import br.com.fiap.postech.adjt.checkout.domain.checkout.CamposMetodoPagamento;
import br.com.fiap.postech.adjt.checkout.domain.checkout.MetodoPagamento;
import br.com.fiap.postech.adjt.checkout.domain.checkout.Pagamento;
import br.com.fiap.postech.adjt.checkout.domain.checkout.StatusPagamento;
import br.com.fiap.postech.adjt.checkout.domain.exception.ApiCartException;
import br.com.fiap.postech.adjt.checkout.domain.exception.ErrorTreatedException;
import br.com.fiap.postech.adjt.checkout.infrastructure.cart.client.CartClient;
import br.com.fiap.postech.adjt.checkout.infrastructure.cart.client.request.CartRequestDTO;
import br.com.fiap.postech.adjt.checkout.infrastructure.cart.client.response.CartResponseDTO;
import br.com.fiap.postech.adjt.checkout.infrastructure.cart.client.response.CartResponseErrorDTO;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto.*;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.model.ItemsOrderEntity;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.model.ItemsOrderId;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.model.OrderAsyncEntity;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.model.OrderEntity;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.repository.ItemsOrderRepository;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.repository.OrderAsyncRepository;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.repository.OrderRepository;
import br.com.fiap.postech.adjt.checkout.infrastructure.payment.client.PaymentClient;
import br.com.fiap.postech.adjt.checkout.infrastructure.payment.client.PaymentClientAsync;
import br.com.fiap.postech.adjt.checkout.infrastructure.payment.client.request.PaymentRequestDTO;
import br.com.fiap.postech.adjt.checkout.infrastructure.payment.client.response.PaymentResponseDTO;
import br.com.fiap.postech.adjt.checkout.useCase.checkout.CheckoutUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class CheckoutUseCaseImpl implements CheckoutUseCase {

    private final CartClient cartClient;
    private final WebClient webClient;
    private final PaymentClient paymentClient;
    private final PaymentClientAsync paymentClientAsync;
    private final OrderRepository orderRepository;
    private final ItemsOrderRepository itemsOrderRepository;
    private final OrderAsyncRepository orderAsyncRepository;
    private final ObjectMapper mapper;
    private final String apiKey;

    public CheckoutUseCaseImpl(final CartClient cartClient,
                               final WebClient webClient,
                               final PaymentClient paymentClient,
                               final PaymentClientAsync paymentClientAsync,
                               final OrderRepository orderRepository,
                               final ItemsOrderRepository itemsOrderRepository,
                               final OrderAsyncRepository orderAsyncRepository,
                               final ObjectMapper mapper,
                               final @Value("${config-api-key}") String apiKey) {
        this.cartClient = cartClient;
        this.webClient = webClient;
        this.paymentClient = paymentClient;
        this.paymentClientAsync = paymentClientAsync;
        this.orderRepository = orderRepository;
        this.itemsOrderRepository = itemsOrderRepository;
        this.orderAsyncRepository = orderAsyncRepository;
        this.mapper = mapper;
        this.apiKey = apiKey;
    }

    @Override
    @Transactional
    public PagamentoResponseDTO processa(SolicitaPagamentoRequestDTO dadosPagamento) {
        final var pagamento = new Pagamento(
                dadosPagamento.consumerId(),
                dadosPagamento.amount(),
                dadosPagamento.currency()
        );
        if(Objects.isNull(dadosPagamento.paymentMethod())) {
            log.error("payment_method NAO PODE SER NULO");
            throw new IllegalArgumentException("payment_method NAO PODE SER NULO");
        }
        final var metodoPagamento = new MetodoPagamento(
                dadosPagamento.paymentMethod().type()
        );
        if(Objects.isNull(dadosPagamento.paymentMethod().fields())) {
            log.error("fields NAO PODE SER NULO");
            throw new IllegalArgumentException("fields NAO PODE SER NULO");
        }
        final var camposMetodoPagamento = new CamposMetodoPagamento(
                dadosPagamento.paymentMethod().fields().number(),
                dadosPagamento.paymentMethod().fields().expirationMonth(),
                dadosPagamento.paymentMethod().fields().expirationYear(),
                dadosPagamento.paymentMethod().fields().cvv(),
                dadosPagamento.paymentMethod().fields().name()
        );

        CartResponseDTO cart = null;
        CartResponseErrorDTO errorResponse = null;
        try {
            cart = this.webClient.method(HttpMethod.GET)
                    .uri("/")
                    .bodyValue(new CartRequestDTO(pagamento.consumerId()))
                    .retrieve()
                    .bodyToMono(CartResponseDTO.class)
                    .block();
        } catch (WebClientResponseException errorExpected) {
            if(errorExpected.getStatusCode() == HttpStatus.BAD_REQUEST
                    && Objects.nonNull(errorExpected.getResponseBodyAsString())) {
                try {
                    errorResponse = this.mapper.readValue(errorExpected.getResponseBodyAsString(), CartResponseErrorDTO.class);
                } catch (Exception e) {
                    log.error("Erro na conversão do JSON de erro", e);
                    throw new ApiCartException("Error converting JSON error");
                }
            } else {
                log.error("Erro não esperado", errorExpected);
                throw new ApiCartException("Error not expected");
            }
        } catch (Exception e) {
            log.error("Problemas com a API de CART ", e);
            throw new ApiCartException("Problemas com a API de CART");
        }

        if(Objects.nonNull(errorResponse)) {
            if(errorResponse.error().equals("Invalid consumerId format")) {
                log.error("ConsumerId invalido. Objeto: {}", errorResponse);
                throw new ErrorTreatedException("Invalid consumerId format");
            } else if (errorResponse.error().equals("Empty cart")) {
                log.error("Carrinho vazio. Objeto: {}", errorResponse);
                throw new ErrorTreatedException("Empty cart");
            } else {
                log.error("Erro não mapeado. Objeto: {}", errorResponse);
                throw new ApiCartException("Unmapped error CART API");
            }
        }

        final var pagamentoPendente = this.orderRepository
                .findByUsuarioAndStatus(pagamento.consumerId(), StatusPagamento.PENDING);
        if(pagamentoPendente.isPresent()) {
            throw new ErrorTreatedException("Payment already pending");
        }

        PaymentResponseDTO executa = null;
        var orderId = UUID.randomUUID();
        StatusPagamento statusPagamento = null;
        try {
            executa = this.paymentClient.executa(
                    new PaymentRequestDTO(
                            pagamento.consumerId(),
                            pagamento.amount(),
                            pagamento.currency(),
                            dadosPagamento.paymentMethod()
                    ),
                    this.apiKey
            );
        } catch (Exception error) {
            log.error("Erro ao processar pagamento", error);
            if(error.getCause() instanceof SocketTimeoutException) {
                statusPagamento = StatusPagamento.PENDING;
                final var orderAsyncEntity = OrderAsyncEntity.builder()
                        .id(UUID.randomUUID())
                        .orderId(orderId)
                        .consumerId(pagamento.consumerId())
                        .amount(pagamento.amount())
                        .currency(pagamento.currency())
                        .paymentMethodType(metodoPagamento.type())
                        .fieldNumber(camposMetodoPagamento.number())
                        .fieldExpirationMonth(camposMetodoPagamento.expirationMonth())
                        .fieldExpirationYear(camposMetodoPagamento.expirationYear())
                        .fieldCvv(camposMetodoPagamento.cvv())
                        .fieldName(camposMetodoPagamento.name())
                        .dataDeCriacao(LocalDateTime.now())
                        .build();
                this.orderAsyncRepository.save(orderAsyncEntity);
            } else {
                log.error("Problemas com a API EXTERNA de pagamento ", error);
                throw new ErrorTreatedException("Problemas com a API EXTERNA de pagamento");
            }
        }

        if(Objects.isNull(statusPagamento)) {
            statusPagamento = StatusPagamento.valueOf(executa.status().toUpperCase());
            orderId = UUID.fromString(executa.orderId());
        }

        final var orderEntity = OrderEntity.builder()
                .id(orderId)
                .usuario(pagamento.consumerId())
                .paymentType(metodoPagamento.type())
                .orderValue(new BigDecimal(pagamento.amount()))
                .status(statusPagamento)
                .dataDeCriacao(LocalDateTime.now())
                .build();
        final var itemsEntity = cart.items()
                .stream()
                .map(item -> ItemsOrderEntity.builder()
                        .id(new ItemsOrderId(orderEntity.getId(), item.itemId()))
                        .quantidade(item.qnt())
                        .build()
                )
                .toList();
        final var orderSalvada = this.orderRepository.save(orderEntity);
        this.itemsOrderRepository.saveAll(itemsEntity);

        this.cartClient.deletaOCarrinho(new CartRequestDTO(pagamento.consumerId()));

        return new PagamentoResponseDTO(
                orderSalvada.getId().toString(),
                statusPagamento.toString()
        );
    }

    @Override
    @Transactional
    public void processaPendentes() {
        final var pagamentosPendentes = this.orderAsyncRepository.findAll();
        pagamentosPendentes
                .forEach(pagamento -> {
                    PaymentResponseDTO executa = null;
                    try {
                        executa = this.paymentClientAsync.executa(
                                new PaymentRequestDTO(
                                        pagamento.getConsumerId(),
                                        pagamento.getAmount(),
                                        pagamento.getCurrency(),
                                        new MetodoPagamentoRequestDTO(
                                                pagamento.getPaymentMethodType(),
                                                new CamposMetodoPagamentoRequestDTO(
                                                        pagamento.getFieldNumber(),
                                                        pagamento.getFieldExpirationMonth(),
                                                        pagamento.getFieldExpirationYear(),
                                                        pagamento.getFieldCvv(),
                                                        pagamento.getFieldName()
                                                )
                                        )
                                ),
                                this.apiKey
                        );
                    } catch (Exception error) {
                        log.error("Erro ao processar pagamento", error);
                        throw new RuntimeException(error);
                    }

                    final var statusPagamento = StatusPagamento.valueOf(executa.status().toUpperCase());
                    final var orderEntity = OrderEntity.builder()
                            .id(pagamento.getOrderId())
                            .usuario(pagamento.getConsumerId())
                            .paymentType(pagamento.getPaymentMethodType())
                            .orderValue(new BigDecimal(pagamento.getAmount()))
                            .status(statusPagamento)
                            .dataDeCriacao(LocalDateTime.now())
                            .build();
                    this.orderRepository.save(orderEntity);
                    this.orderAsyncRepository.delete(pagamento);
                });

    }

    @Override
    public BuscaListaPagamentoResponseDTO busca(final String consumerId) {
        final var pagamentos = this.orderRepository.findByUsuario(consumerId);
        if(Objects.nonNull(pagamentos) && !pagamentos.isEmpty()) {
            final var itemsEntity = pagamentos
                    .stream()
                    .map(item -> {
                        final var items = this.itemsOrderRepository.findByIdIdOrder(item.getId());
                        return new BuscaPagamentoResponseDTO(
                                item.getId().toString(),
                                this.pegaItems(items),
                                item.getPaymentType(),
                                item.getOrderValue(),
                                item.getStatus().toString()
                        );
                    })
                    .toList();
            return new BuscaListaPagamentoResponseDTO(itemsEntity);
        }
        throw new ErrorTreatedException("Order not found");
    }

    private List<ItensPagamentoResponseDTO> pegaItems(final List<ItemsOrderEntity> items) {
        if(Objects.nonNull(items)) {
            return items
                    .stream()
                    .map(item -> new ItensPagamentoResponseDTO(
                            item.getId().getEan(),
                            item.getQuantidade()
                    ))
                    .toList();
        }
        return List.of();
    }

    @Override
    public BuscaPagamentoResponseDTO buscaPorOrderId(final String orderId) {
        final var pagamento = this.orderRepository.findById(UUID.fromString(orderId));
        if(pagamento.isPresent()) {
            final var item = pagamento.get();
            final var items = this.itemsOrderRepository.findByIdIdOrder(UUID.fromString(orderId));
            return new BuscaPagamentoResponseDTO(
                    orderId,
                    this.pegaItems(items),
                    item.getPaymentType(),
                    item.getOrderValue(),
                    item.getStatus().toString()
            );
        }
        throw new ErrorTreatedException("Order not found");
    }

}
