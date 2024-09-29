package br.com.fiap.postech.adjt.checkout.integrados;

import br.com.fiap.postech.adjt.checkout.domain.checkout.StatusPagamento;
import br.com.fiap.postech.adjt.checkout.infrastructure.cart.client.CartClient;
import br.com.fiap.postech.adjt.checkout.infrastructure.cart.client.request.CartRequestDTO;
import br.com.fiap.postech.adjt.checkout.infrastructure.cart.client.response.CartDetailsResponseDTO;
import br.com.fiap.postech.adjt.checkout.infrastructure.cart.client.response.CartResponseDTO;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.dto.*;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.model.ItemsOrderEntity;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.model.ItemsOrderId;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.model.OrderEntity;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.repository.ItemsOrderRepository;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.repository.OrderAsyncRepository;
import br.com.fiap.postech.adjt.checkout.infrastructure.checkout.repository.OrderRepository;
import br.com.fiap.postech.adjt.checkout.infrastructure.payment.client.PaymentClient;
import br.com.fiap.postech.adjt.checkout.infrastructure.payment.client.PaymentClientAsync;
import br.com.fiap.postech.adjt.checkout.infrastructure.payment.client.response.PaymentResponseDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.CheckoutController.URL_CONSUMER_ID;
import static br.com.fiap.postech.adjt.checkout.infrastructure.checkout.controller.CheckoutController.URL_ORDER_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CheckoutControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @MockBean
    CartClient cartClient;

    @Autowired
    @MockBean
    WebClient webClient;

    @Autowired
    @MockBean
    PaymentClient paymentClient;

    @Autowired
    @MockBean
    PaymentClientAsync paymentClientAsync;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemsOrderRepository itemsOrderRepository;

    @Autowired
    OrderAsyncRepository orderAsyncRepository;

    @BeforeEach
    void inicializaLimpezaDoDatabase() {
        this.orderRepository.deleteAll();
        this.itemsOrderRepository.deleteAll();
        this.orderAsyncRepository.deleteAll();
    }

    @AfterAll
    void finalizaLimpezaDoDatabase() {
        this.orderRepository.deleteAll();
        this.itemsOrderRepository.deleteAll();
        this.orderAsyncRepository.deleteAll();
    }

    @Test
    public void processa_deveRetornar400_carrinhoVazio_naoSalvaNaBaseDeDados() throws Exception {
        var requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        var requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        var responseSpec = mock(WebClient.ResponseSpec.class);

        final var responseError = """
                {
                    "error": "Empty cart"
                }
                """;
        when(webClient.method(eq(HttpMethod.GET)))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(eq("/")))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any(CartRequestDTO.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve())
                .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CartResponseDTO.class))
                .thenReturn(Mono.error(new WebClientResponseException(400, "Empty cart", null, responseError.getBytes(), null)));

        final var request = new SolicitaPagamentoRequestDTO(
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
        );
        final var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        final var jsonRequest = objectMapper.writeValueAsString(request);

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest()
                )
                .andReturn();

        final var responseAppString = response.getResponse().getContentAsString();
        final var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<ErrorHandlingResponseDTO>() {});

        Assertions.assertEquals("Empty cart", responseApp.error());

        this.orderRepository.deleteAll();
        this.itemsOrderRepository.deleteAll();
        this.orderAsyncRepository.deleteAll();

        Assertions.assertEquals(0, this.orderRepository.findAll().size());
        Assertions.assertEquals(0, this.itemsOrderRepository.findAll().size());
        Assertions.assertEquals(0, this.orderAsyncRepository.findAll().size());
    }

    @Test
    public void processa_deveRetornar400_indisponibilidadeDaApiCart_naoSalvaNaBaseDeDados() throws Exception {
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

        final var request = new SolicitaPagamentoRequestDTO(
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
        );
        final var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        final var jsonRequest = objectMapper.writeValueAsString(request);

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest()
                )
                .andReturn();

        final var responseAppString = response.getResponse().getContentAsString();
        final var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<ErrorHandlingResponseDTO>() {});

        Assertions.assertEquals("Problemas com a API de CART", responseApp.error());

        this.orderRepository.deleteAll();
        this.itemsOrderRepository.deleteAll();
        this.orderAsyncRepository.deleteAll();

        Assertions.assertEquals(0, this.orderRepository.findAll().size());
        Assertions.assertEquals(0, this.itemsOrderRepository.findAll().size());
        Assertions.assertEquals(0, this.orderAsyncRepository.findAll().size());
    }

    @Test
    public void processa_deveRetornar400_erroNaConversaoDaMensagemDeErroDaApiCart_naoSalvaNaBaseDeDados() throws Exception {
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
                .thenReturn(Mono.error(new WebClientResponseException(400, "Empty cart", null, "teste".getBytes(), null)));

        final var request = new SolicitaPagamentoRequestDTO(
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
        );
        final var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        final var jsonRequest = objectMapper.writeValueAsString(request);

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest()
                )
                .andReturn();

        final var responseAppString = response.getResponse().getContentAsString();
        final var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<ErrorHandlingResponseDTO>() {});

        Assertions.assertEquals("Error converting JSON error", responseApp.error());

        this.orderRepository.deleteAll();
        this.itemsOrderRepository.deleteAll();
        this.orderAsyncRepository.deleteAll();

        Assertions.assertEquals(0, this.orderRepository.findAll().size());
        Assertions.assertEquals(0, this.itemsOrderRepository.findAll().size());
        Assertions.assertEquals(0, this.orderAsyncRepository.findAll().size());
    }

    @Test
    public void processa_deveRetornar400_erroNaoEsperadoDaApiCart_naoSalvaNaBaseDeDados() throws Exception {
        var requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        var requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        var responseSpec = mock(WebClient.ResponseSpec.class);

        final var responseError = """
                {
                    "error": "Empty cart"
                }
                """;
        when(webClient.method(eq(HttpMethod.GET)))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(eq("/")))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any(CartRequestDTO.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve())
                .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CartResponseDTO.class))
                .thenReturn(Mono.error(new WebClientResponseException(500, "ERROR NOT MAPPED", null, responseError.getBytes(), null)));

        final var request = new SolicitaPagamentoRequestDTO(
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
        );
        final var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        final var jsonRequest = objectMapper.writeValueAsString(request);

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest()
                )
                .andReturn();

        final var responseAppString = response.getResponse().getContentAsString();
        final var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<ErrorHandlingResponseDTO>() {});

        Assertions.assertEquals("Error not expected", responseApp.error());

        this.orderRepository.deleteAll();
        this.itemsOrderRepository.deleteAll();
        this.orderAsyncRepository.deleteAll();

        Assertions.assertEquals(0, this.orderRepository.findAll().size());
        Assertions.assertEquals(0, this.itemsOrderRepository.findAll().size());
        Assertions.assertEquals(0, this.orderAsyncRepository.findAll().size());
    }

    @Test
    public void processa_deveRetornar400_erroNaoMapeadoDaApiCart_naoSalvaNaBaseDeDados() throws Exception {
        var requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        var requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        var responseSpec = mock(WebClient.ResponseSpec.class);

        final var responseError = """
                {
                    "error": "new error"
                }
                """;
        when(webClient.method(eq(HttpMethod.GET)))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(eq("/")))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any(CartRequestDTO.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve())
                .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CartResponseDTO.class))
                .thenReturn(Mono.error(new WebClientResponseException(400, "Novo erro", null, responseError.getBytes(), null)));

        final var request = new SolicitaPagamentoRequestDTO(
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
        );
        final var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        final var jsonRequest = objectMapper.writeValueAsString(request);

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest()
                )
                .andReturn();

        final var responseAppString = response.getResponse().getContentAsString();
        final var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<ErrorHandlingResponseDTO>() {});

        Assertions.assertEquals("Unmapped error CART API", responseApp.error());

        this.orderRepository.deleteAll();
        this.itemsOrderRepository.deleteAll();
        this.orderAsyncRepository.deleteAll();

        Assertions.assertEquals(0, this.orderRepository.findAll().size());
        Assertions.assertEquals(0, this.itemsOrderRepository.findAll().size());
        Assertions.assertEquals(0, this.orderAsyncRepository.findAll().size());
    }

    @Test
    public void processa_deveRetornar400_existePagamentoPendente_naoSalvaNaBaseDeDados() throws Exception {
        var requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        var requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        var responseSpec = mock(WebClient.ResponseSpec.class);

        CartResponseDTO responseDTO = new CartResponseDTO(
                List.of(new CartDetailsResponseDTO(
                        1L,
                        2L
                ))
        );
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

        final var orderId = UUID.randomUUID();
        this.orderRepository.save(
                new OrderEntity(
                        orderId,
                        "e7c5c208-c4c3-42fc-9370-3141309cb7d0",
                        "cartao de credito",
                        new BigDecimal("100.00"),
                        StatusPagamento.PENDING,
                        LocalDateTime.now()
                )
        );

        final var request = new SolicitaPagamentoRequestDTO(
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
        );
        final var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        final var jsonRequest = objectMapper.writeValueAsString(request);

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest()
                )
                .andReturn();

        final var responseAppString = response.getResponse().getContentAsString();
        final var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<ErrorHandlingResponseDTO>() {});

        Assertions.assertEquals("Payment already pending", responseApp.error());

        final var orderEntity = this.orderRepository.findAll().get(0);

        Assertions.assertEquals(1, this.orderRepository.findAll().size());
        Assertions.assertEquals(0, this.itemsOrderRepository.findAll().size());
        Assertions.assertEquals(0, this.orderAsyncRepository.findAll().size());

        Assertions.assertEquals(orderId, orderEntity.getId());
        Assertions.assertEquals("e7c5c208-c4c3-42fc-9370-3141309cb7d0", orderEntity.getUsuario());
        Assertions.assertEquals("cartao de credito", orderEntity.getPaymentType());
        Assertions.assertEquals(new BigDecimal("100.00"), orderEntity.getOrderValue());
        Assertions.assertEquals(StatusPagamento.PENDING, orderEntity.getStatus());
        Assertions.assertNotNull(orderEntity.getDataDeCriacao());
    }

    @Test
    public void processa_deveRetornar400_indisponibilidadeDaApiExternaDePagamento_naoSalvaNaBaseDeDados() throws Exception {
        var requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        var requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        var responseSpec = mock(WebClient.ResponseSpec.class);

        CartResponseDTO responseDTO = new CartResponseDTO(
                List.of(new CartDetailsResponseDTO(
                        1L,
                        2L
                ))
        );
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

        Mockito.doThrow(
                        new RuntimeException("api indisponivel")
                )
                .when(paymentClient)
                .executa(
                        Mockito.any(),
                        Mockito.any()
                );

        final var request = new SolicitaPagamentoRequestDTO(
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
        );
        final var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        final var jsonRequest = objectMapper.writeValueAsString(request);

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest()
                )
                .andReturn();

        final var responseAppString = response.getResponse().getContentAsString();
        final var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<ErrorHandlingResponseDTO>() {});

        Assertions.assertEquals("Problemas com a API EXTERNA de pagamento", responseApp.error());

        Assertions.assertEquals(0, this.orderRepository.findAll().size());
        Assertions.assertEquals(0, this.itemsOrderRepository.findAll().size());
        Assertions.assertEquals(0, this.orderAsyncRepository.findAll().size());
    }

    @Test
    public void processa_deveRetornar200_timeoutDe300msDaApiExternaDePagamento_salvaNaBaseDeDados() throws Exception {
        var requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        var requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        var responseSpec = mock(WebClient.ResponseSpec.class);

        CartResponseDTO responseDTO = new CartResponseDTO(
                List.of(new CartDetailsResponseDTO(
                        1L,
                        2L
                ))
        );
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

        Mockito.doThrow(
                        new RuntimeException(new SocketTimeoutException("timeout"))
                )
                .when(paymentClient)
                .executa(
                        Mockito.any(),
                        Mockito.any()
                );

        Mockito.when(cartClient.deletaOCarrinho(Mockito.any()))
                .thenReturn(
                        null
                );

        final var request = new SolicitaPagamentoRequestDTO(
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
        );
        final var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        final var jsonRequest = objectMapper.writeValueAsString(request);

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                )
                .andReturn();

        final var responseAppString = response.getResponse().getContentAsString();
        final var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<PagamentoResponseDTO>() {});

        final var orderEntity = this.orderRepository.findAll().get(0);
        final var itemsOrderEntity = this.itemsOrderRepository.findAll().get(0);
        final var orderAsyncEntity = this.orderAsyncRepository.findAll().get(0);

        Assertions.assertEquals(1, this.orderRepository.findAll().size());
        Assertions.assertEquals(1, this.itemsOrderRepository.findAll().size());
        Assertions.assertEquals(1, this.orderAsyncRepository.findAll().size());

        Assertions.assertEquals("e7c5c208-c4c3-42fc-9370-3141309cb7d0", orderEntity.getUsuario());
        Assertions.assertEquals("br_credit_card", orderEntity.getPaymentType());
        Assertions.assertEquals(new BigDecimal("10.00"), orderEntity.getOrderValue());
        Assertions.assertEquals(StatusPagamento.PENDING, orderEntity.getStatus());
        Assertions.assertNotNull(orderEntity.getDataDeCriacao());

        Assertions.assertEquals(orderEntity.getId(), itemsOrderEntity.getId().getIdOrder());
        Assertions.assertEquals(1L, itemsOrderEntity.getId().getEan());
        Assertions.assertEquals(2L, itemsOrderEntity.getQuantidade());

        Assertions.assertEquals("e7c5c208-c4c3-42fc-9370-3141309cb7d0", orderAsyncEntity.getConsumerId());
        Assertions.assertEquals(10L, orderAsyncEntity.getAmount());
        Assertions.assertEquals("BRL", orderAsyncEntity.getCurrency());
        Assertions.assertEquals("br_credit_card", orderAsyncEntity.getPaymentMethodType());
        Assertions.assertEquals("123456", orderAsyncEntity.getFieldNumber());
        Assertions.assertEquals("12", orderAsyncEntity.getFieldExpirationMonth());
        Assertions.assertEquals("25", orderAsyncEntity.getFieldExpirationYear());
        Assertions.assertEquals("123", orderAsyncEntity.getFieldCvv());
        Assertions.assertEquals("Nome Teste", orderAsyncEntity.getFieldName());
        Assertions.assertNotNull(orderAsyncEntity.getDataDeCriacao());

        Assertions.assertEquals(orderEntity.getId().toString(), responseApp.orderId());
        Assertions.assertEquals(StatusPagamento.PENDING.name(), responseApp.status());
    }

    @Test
    public void processa_deveRetornar200_apiExternaDePagamentoRespondeNormal_salvaNaBaseDeDados() throws Exception {
        var requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        var requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        var responseSpec = mock(WebClient.ResponseSpec.class);

        CartResponseDTO responseDTO = new CartResponseDTO(
                List.of(new CartDetailsResponseDTO(
                        1L,
                        2L
                ))
        );
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

        final var orderId = UUID.randomUUID();
        Mockito.when(paymentClient.executa(any(), any()))
                .thenReturn(
                        new PaymentResponseDTO(
                                orderId.toString(),
                                StatusPagamento.APPROVED.name()
                        )
                );

        Mockito.when(cartClient.deletaOCarrinho(Mockito.any()))
                .thenReturn(
                        null
                );

        final var request = new SolicitaPagamentoRequestDTO(
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
        );
        final var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        final var jsonRequest = objectMapper.writeValueAsString(request);

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                )
                .andReturn();

        final var responseAppString = response.getResponse().getContentAsString();
        final var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<PagamentoResponseDTO>() {});

        final var orderEntity = this.orderRepository.findAll().get(0);
        final var itemsOrderEntity = this.itemsOrderRepository.findAll().get(0);

        Assertions.assertEquals(1, this.orderRepository.findAll().size());
        Assertions.assertEquals(1, this.itemsOrderRepository.findAll().size());
        Assertions.assertEquals(0, this.orderAsyncRepository.findAll().size());

        Assertions.assertEquals(orderId, orderEntity.getId());
        Assertions.assertEquals("e7c5c208-c4c3-42fc-9370-3141309cb7d0", orderEntity.getUsuario());
        Assertions.assertEquals("br_credit_card", orderEntity.getPaymentType());
        Assertions.assertEquals(new BigDecimal("10.00"), orderEntity.getOrderValue());
        Assertions.assertEquals(StatusPagamento.APPROVED, orderEntity.getStatus());
        Assertions.assertNotNull(orderEntity.getDataDeCriacao());

        Assertions.assertEquals(orderEntity.getId(), itemsOrderEntity.getId().getIdOrder());
        Assertions.assertEquals(1L, itemsOrderEntity.getId().getEan());
        Assertions.assertEquals(2L, itemsOrderEntity.getQuantidade());

        Assertions.assertEquals(orderEntity.getId().toString(), responseApp.orderId());
        Assertions.assertEquals(StatusPagamento.APPROVED.name(), responseApp.status());
    }

    @Test
    public void busca_deveRetornar200_sucessoComMaisDeUmPagamento_buscaNaBaseDeDados() throws Exception {
        final var orderId = UUID.randomUUID();
        final var orderId2 = UUID.randomUUID();
        this.orderRepository.saveAll(
                List.of(
                        new OrderEntity(
                                orderId,
                                "e7c5c208-c4c3-42fc-9370-3141309cb7d0",
                                "cartao de credito",
                                new BigDecimal("100.00"),
                                StatusPagamento.PENDING,
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

        this.itemsOrderRepository.saveAll(
                List.of(
                        new ItemsOrderEntity(
                                new ItemsOrderId(orderId, 1L),
                                3L
                        ),
                        new ItemsOrderEntity(
                                new ItemsOrderId(orderId, 2L),
                                4L
                        ),
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

        Mockito.when(cartClient.deletaOCarrinho(Mockito.any()))
                .thenReturn(
                        null
                );

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.get(URL_CONSUMER_ID.replace("{consumerId}", "e7c5c208-c4c3-42fc-9370-3141309cb7d0"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                )
                .andReturn();

        final var responseAppString = response.getResponse().getContentAsString();
        final var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<BuscaListaPagamentoResponseDTO>() {});

        Assertions.assertEquals(2, this.orderRepository.findAll().size());
        Assertions.assertEquals(4, this.itemsOrderRepository.findAll().size());
        Assertions.assertEquals(0, this.orderAsyncRepository.findAll().size());

        Assertions.assertEquals(2, responseApp.orders().size());
    }

    @Test
    public void busca_deveRetornar200_sucessoComUmPagamentoEUmItem_buscaNaBaseDeDados() throws Exception {
        final var orderId = UUID.randomUUID();
        this.orderRepository.save(
                    new OrderEntity(
                            orderId,
                            "e7c5c208-c4c3-42fc-9370-3141309cb7d0",
                            "cartao de credito",
                            new BigDecimal("100.00"),
                            StatusPagamento.PENDING,
                            LocalDateTime.now()
                    )
        );

        this.itemsOrderRepository.save(
                    new ItemsOrderEntity(
                            new ItemsOrderId(orderId, 1L),
                            3L
                    )
        );

        Mockito.when(cartClient.deletaOCarrinho(Mockito.any()))
                .thenReturn(
                        null
                );

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.get(URL_CONSUMER_ID.replace("{consumerId}", "e7c5c208-c4c3-42fc-9370-3141309cb7d0"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                )
                .andReturn();

        final var responseAppString = response.getResponse().getContentAsString();
        final var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<BuscaListaPagamentoResponseDTO>() {});

        Assertions.assertEquals(1, this.orderRepository.findAll().size());
        Assertions.assertEquals(1, this.itemsOrderRepository.findAll().size());
        Assertions.assertEquals(0, this.orderAsyncRepository.findAll().size());

        Assertions.assertEquals(orderId.toString(), responseApp.orders().get(0).orderId());
        Assertions.assertEquals(1L, responseApp.orders().get(0).items().get(0).itemId());
        Assertions.assertEquals(3L, responseApp.orders().get(0).items().get(0).qnt());
        Assertions.assertEquals("cartao de credito", responseApp.orders().get(0).paymentType());
        Assertions.assertEquals(new BigDecimal("100.00"), responseApp.orders().get(0).value());
        Assertions.assertEquals(StatusPagamento.PENDING.name(), responseApp.orders().get(0).paymentStatus());
    }

    @Test
    public void busca_deveRetornar400_semPagamento_buscaNaBaseDeDados() throws Exception {
        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.get(URL_CONSUMER_ID.replace("{consumerId}", "e7c5c208-c4c3-42fc-9370-3141309cb7d0"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest()
                )
                .andReturn();

        final var responseAppString = response.getResponse().getContentAsString();
        final var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<ErrorHandlingResponseDTO>() {});

        Assertions.assertEquals(0, this.orderRepository.findAll().size());
        Assertions.assertEquals(0, this.itemsOrderRepository.findAll().size());
        Assertions.assertEquals(0, this.orderAsyncRepository.findAll().size());

        Assertions.assertEquals("Order not found", responseApp.error());
    }

    @Test
    public void buscaPorOrderId_deveRetornar200_sucesso_buscaNaBaseDeDados() throws Exception {
        final var orderId = UUID.randomUUID();
        final var orderId2 = UUID.randomUUID();
        this.orderRepository.saveAll(
                List.of(
                        new OrderEntity(
                                orderId,
                                "e7c5c208-c4c3-42fc-9370-3141309cb7d0",
                                "cartao de credito",
                                new BigDecimal("100.00"),
                                StatusPagamento.PENDING,
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

        this.itemsOrderRepository.saveAll(
                List.of(
                        new ItemsOrderEntity(
                                new ItemsOrderId(orderId, 1L),
                                3L
                        ),
                        new ItemsOrderEntity(
                                new ItemsOrderId(orderId2, 2L),
                                4L
                        )
                )
        );

        Mockito.when(cartClient.deletaOCarrinho(Mockito.any()))
                .thenReturn(
                        null
                );

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.get(URL_ORDER_ID.replace("{orderId}", orderId.toString()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                )
                .andReturn();

        final var responseAppString = response.getResponse().getContentAsString();
        final var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<BuscaPagamentoResponseDTO>() {});

        Assertions.assertEquals(2, this.orderRepository.findAll().size());
        Assertions.assertEquals(2, this.itemsOrderRepository.findAll().size());
        Assertions.assertEquals(0, this.orderAsyncRepository.findAll().size());

        Assertions.assertEquals(orderId.toString(), responseApp.orderId());
        Assertions.assertEquals(1L, responseApp.items().get(0).itemId());
        Assertions.assertEquals(3L, responseApp.items().get(0).qnt());
        Assertions.assertEquals("cartao de credito", responseApp.paymentType());
        Assertions.assertEquals(new BigDecimal("100.00"), responseApp.value());
        Assertions.assertEquals(StatusPagamento.PENDING.name(), responseApp.paymentStatus());
    }

    @Test
    public void buscaPorOrderId_deveRetornar400_semPagamento_buscaNaBaseDeDados() throws Exception {
        final var orderId = UUID.randomUUID();
        final var orderId2 = UUID.randomUUID();
        this.orderRepository.saveAll(
                List.of(
                        new OrderEntity(
                                orderId,
                                "e7c5c208-c4c3-42fc-9370-3141309cb7d0",
                                "cartao de credito",
                                new BigDecimal("100.00"),
                                StatusPagamento.PENDING,
                                LocalDateTime.now()
                        )
                )
        );

        this.itemsOrderRepository.saveAll(
                List.of(
                        new ItemsOrderEntity(
                                new ItemsOrderId(orderId, 1L),
                                3L
                        )
                )
        );

        Mockito.when(cartClient.deletaOCarrinho(Mockito.any()))
                .thenReturn(
                        null
                );

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.get(URL_ORDER_ID.replace("{orderId}", orderId2.toString()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest()
                )
                .andReturn();

        final var responseAppString = response.getResponse().getContentAsString();
        final var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<ErrorHandlingResponseDTO>() {});

        Assertions.assertEquals(1, this.orderRepository.findAll().size());
        Assertions.assertEquals(1, this.itemsOrderRepository.findAll().size());
        Assertions.assertEquals(0, this.orderAsyncRepository.findAll().size());

        Assertions.assertEquals("Order not found", responseApp.error());
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
                                         final String name) throws Exception {
        final var request = new SolicitaPagamentoRequestDTO(
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
        );
        final var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        final var jsonRequest = objectMapper.writeValueAsString(request);

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest()
                )
                .andReturn();

        final var responseAppString = response.getResponse().getContentAsString();
        final var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<ErrorHandlingResponseDTO>() {});

        Assertions.assertEquals("Invalid payment information", responseApp.error());

        Assertions.assertEquals(0, this.orderRepository.findAll().size());
        Assertions.assertEquals(0, this.itemsOrderRepository.findAll().size());
        Assertions.assertEquals(0, this.orderAsyncRepository.findAll().size());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "-1",
            "teste",
            " ",
            "",
            "e7c5c208-c4c3-42fc-9370-3141309cb7OI"
    })
    public void processa_consumerIdInvalido(final String consumerId) throws Exception {
        var requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        var requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        var responseSpec = mock(WebClient.ResponseSpec.class);

        final var responseError = """
                {
                    "error": "Invalid consumerId format"
                }
                """;
        when(webClient.method(eq(HttpMethod.GET)))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(eq("/")))
                .thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any(CartRequestDTO.class)))
                .thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve())
                .thenReturn(responseSpec);
        when(responseSpec.bodyToMono(CartResponseDTO.class))
                .thenReturn(Mono.error(new WebClientResponseException(400, "Invalid consumerId format", null, responseError.getBytes(), null)));

        final var request = new SolicitaPagamentoRequestDTO(
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
        );
        final var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        final var jsonRequest = objectMapper.writeValueAsString(request);

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest()
                )
                .andReturn();

        final var responseAppString = response.getResponse().getContentAsString();
        final var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<ErrorHandlingResponseDTO>() {});

        Assertions.assertEquals("Invalid consumerId format", responseApp.error());
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
