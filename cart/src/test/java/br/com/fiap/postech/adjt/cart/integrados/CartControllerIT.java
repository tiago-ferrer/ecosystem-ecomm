package br.com.fiap.postech.adjt.cart.integrados;

import br.com.fiap.postech.adjt.cart.domain.cart.StatusEnum;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto.AdicionaItemRequestDTO;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto.ErrorHandlingResponseDTO;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto.ItemResponseDTO;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.model.CarrinhoEntity;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.model.ItensNoCarrinhoEntity;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.model.ItensNoCarrinhoId;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.repository.CarrinhoRepository;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.repository.ItensNoCarrinhoRepository;
import br.com.fiap.postech.adjt.cart.infrastructure.products.client.ProductsClient;
import br.com.fiap.postech.adjt.cart.infrastructure.products.client.response.ProductsResponseDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import static br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.CartController.URL_ITEMS;


@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CartControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @MockBean
    ProductsClient productsClient;

    @Autowired
    CarrinhoRepository repositoryCarrinho;

    @Autowired
    ItensNoCarrinhoRepository repositoryItensNoCarrinho;

    @BeforeEach
    void inicializaLimpezaDoDatabase() {
        this.repositoryCarrinho.deleteAll();
        this.repositoryItensNoCarrinho.deleteAll();
    }

    @AfterAll
    void finalizaLimpezaDoDatabase() {
        this.repositoryCarrinho.deleteAll();
        this.repositoryItensNoCarrinho.deleteAll();
    }

    @Test
    public void adiciona_deveRetornar200_carrinhoVazio_salvaNaBaseDeDados() throws Exception {
        Mockito.when(this.productsClient.busca(1L, 2L))
                .thenReturn(
                        new ProductsResponseDTO(
                                1L,
                                "nome do produto",
                                new BigDecimal("100.00")
                        )
                );

        final var request = new AdicionaItemRequestDTO(
                "e7c5c208-c4c3-42fc-9370-3141309cb7bc",
                1L,
                2L
        );
        final var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        final var jsonRequest = objectMapper.writeValueAsString(request);

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.post(URL_ITEMS)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                )
                .andReturn();

        final var responseAppString = response.getResponse().getContentAsString();
        final var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<ItemResponseDTO>() {});

        Assertions.assertEquals("Item added to cart successfully", responseApp.message());

        var carrinho = this.repositoryCarrinho.findAll().get(0);
        var itensDoCarrinho = this.repositoryItensNoCarrinho.findAll();

        Assertions.assertEquals(StatusEnum.ABERTO, carrinho.getStatus());
        Assertions.assertEquals("e7c5c208-c4c3-42fc-9370-3141309cb7bc", carrinho.getUsuario());
        Assertions.assertEquals(new BigDecimal("200.00"), carrinho.getValorTotal());
        Assertions.assertNotNull(carrinho.getDataDeCriacao());

        Assertions.assertEquals(1, itensDoCarrinho.size());
        Assertions.assertEquals(new BigDecimal("100.00"), itensDoCarrinho.get(0).getPrecoUnitario());
        Assertions.assertEquals(2, itensDoCarrinho.get(0).getQuantidade());
        Assertions.assertEquals(1L, itensDoCarrinho.get(0).getId().getEan());
        Assertions.assertEquals(carrinho.getId(), itensDoCarrinho.get(0).getId().getIdCarrinho());
    }

    @Test
    public void adiciona_deveRetornar400_indisponibilidadeDaApiExterna_naoSalvaNaBaseDeDados() throws Exception {
        Mockito.doThrow(
                        new IllegalArgumentException("API EXTERNA COM PROBLEMAS!")
                )
                .when(this.productsClient)
                .busca(
                        Mockito.any(),
                        Mockito.any()
                );

        final var request = new AdicionaItemRequestDTO(
                "e7c5c208-c4c3-42fc-9370-3141309cb7bc",
                1L,
                2L
        );
        final var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        final var jsonRequest = objectMapper.writeValueAsString(request);

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.post(URL_ITEMS)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest()
                )
                .andReturn();

        final var responseAppString = response.getResponse().getContentAsString();
        final var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<ErrorHandlingResponseDTO>() {
                });

        Assertions.assertEquals("Problemas com a API de PRODUCTS", responseApp.error());

        Assertions.assertEquals(0, this.repositoryCarrinho.findAll().size());
        Assertions.assertEquals(0, this.repositoryItensNoCarrinho.findAll().size());
    }

    @Test
    public void adiciona_deveRetornar400_itemNaoEncontrado_naoSalvaNaBaseDeDados() throws Exception {
        var requestClient = Request.create(
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
                                requestClient,
                                """
                                {
                                    "codeError": 1,
                                    "message": "Item não encontrado"
                                }
                                """.getBytes(),
                                Map.of()
                        )
                )
                .when(productsClient)
                .busca(
                        1L,
                        2L
                );

        final var request = new AdicionaItemRequestDTO(
                "e7c5c208-c4c3-42fc-9370-3141309cb7bc",
                1L,
                2L
        );
        final var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        final var jsonRequest = objectMapper.writeValueAsString(request);

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.post(URL_ITEMS)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest()
                )
                .andReturn();

        final var responseAppString = response.getResponse().getContentAsString();
        final var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<ErrorHandlingResponseDTO>() {
                });

        Assertions.assertEquals("Invalid itemId does not exist", responseApp.error());

        Assertions.assertEquals(0, this.repositoryCarrinho.findAll().size());
        Assertions.assertEquals(0, this.repositoryItensNoCarrinho.findAll().size());
    }

    @Test
    public void adiciona_deveRetornar400_quantidadeInvalida_naoSalvaNaBaseDeDados() throws Exception {
        var requestClient = Request.create(
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
                                requestClient,
                                """
                                {
                                    "codeError": 2,
                                    "message": "Quantide invalida"
                                }
                                """.getBytes(),
                                Map.of()
                        )
                )
                .when(productsClient)
                .busca(
                        1L,
                        2L
                );

        final var request = new AdicionaItemRequestDTO(
                "e7c5c208-c4c3-42fc-9370-3141309cb7bc",
                1L,
                2L
        );
        final var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        final var jsonRequest = objectMapper.writeValueAsString(request);

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.post(URL_ITEMS)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest()
                )
                .andReturn();

        final var responseAppString = response.getResponse().getContentAsString();
        final var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<ErrorHandlingResponseDTO>() {
                });

        Assertions.assertEquals("Invalid itemId quantity", responseApp.error());

        Assertions.assertEquals(0, this.repositoryCarrinho.findAll().size());
        Assertions.assertEquals(0, this.repositoryItensNoCarrinho.findAll().size());
    }

    @Test
    public void adiciona_deveRetornar400_erroNaoMapeado_naoSalvaNaBaseDeDados() throws Exception {
        var requestClient = Request.create(
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
                                requestClient,
                                """
                                {
                                    "codeError": 350,
                                    "message": "error nao mapeado"
                                }
                                """.getBytes(),
                                Map.of()
                        )
                )
                .when(productsClient)
                .busca(
                        1L,
                        2L
                );

        final var request = new AdicionaItemRequestDTO(
                "e7c5c208-c4c3-42fc-9370-3141309cb7bc",
                1L,
                2L
        );
        final var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        final var jsonRequest = objectMapper.writeValueAsString(request);

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.post(URL_ITEMS)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest()
                )
                .andReturn();

        final var responseAppString = response.getResponse().getContentAsString();
        final var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<ErrorHandlingResponseDTO>() {
                });

        Assertions.assertEquals("Unmapped error PRODUCTS API", responseApp.error());

        Assertions.assertEquals(0, this.repositoryCarrinho.findAll().size());
        Assertions.assertEquals(0, this.repositoryItensNoCarrinho.findAll().size());
    }

    @Test
    public void adiciona_deveRetornar200_carrinhoExiste_salvaNaBaseDeDados() throws Exception {
        final var carrinhoSalvo = this.repositoryCarrinho.save(
                CarrinhoEntity.builder()
                        .usuario("e7c5c208-c4c3-42fc-9370-3141309cb7bc")
                        .status(StatusEnum.ABERTO)
                        .dataDeCriacao(LocalDateTime.now())
                        .valorTotal(new BigDecimal("500.00"))
                        .build()
        );
        this.repositoryItensNoCarrinho.save(
                ItensNoCarrinhoEntity.builder()
                        .id(ItensNoCarrinhoId.builder()
                                .idCarrinho(carrinhoSalvo.getId())
                                .ean(7894900011517L)
                                .build())
                        .precoUnitario(new BigDecimal("500.00"))
                        .quantidade(5L)
                        .build()
        );

        Mockito.when(this.productsClient.busca(1L, 2L))
                .thenReturn(
                        new ProductsResponseDTO(
                                1L,
                                "nome do produto",
                                new BigDecimal("100.00")
                        )
                );

        final var request = new AdicionaItemRequestDTO(
                "e7c5c208-c4c3-42fc-9370-3141309cb7bc",
                1L,
                2L
        );
        final var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        final var jsonRequest = objectMapper.writeValueAsString(request);

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.post(URL_ITEMS)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                )
                .andReturn();

        final var responseAppString = response.getResponse().getContentAsString();
        final var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<ItemResponseDTO>() {});

        Assertions.assertEquals("Item added to cart successfully", responseApp.message());

        var carrinho = this.repositoryCarrinho.findAll().get(0);
        var itensDoCarrinho = this.repositoryItensNoCarrinho.findAll();

        Assertions.assertEquals(1, this.repositoryCarrinho.findAll().size());

        Assertions.assertEquals(StatusEnum.ABERTO, carrinho.getStatus());
        Assertions.assertEquals("e7c5c208-c4c3-42fc-9370-3141309cb7bc", carrinho.getUsuario());
        Assertions.assertEquals(new BigDecimal("2700.00"), carrinho.getValorTotal());
        Assertions.assertNotNull(carrinho.getDataDeCriacao());

        Assertions.assertEquals(2, itensDoCarrinho.size());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "-1",
            "teste",
            " ",
            "",
            "e7c5c208-c4c3-42fc-9370-3141309cb7OI"
    })
    public void adiciona_consumerIdInvalido(final String consumerId) throws Exception {
        final var request = new AdicionaItemRequestDTO(
                consumerId.equals("-1") ? null : consumerId,
                123456L,
                1L
        );
        final var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        final var jsonRequest = objectMapper.writeValueAsString(request);

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.post(URL_ITEMS)
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

    @ParameterizedTest
    @ValueSource(strings = {
            "-1",
            "teste",
            " ",
            "",
            "e7c5c208-c4c3-42fc-9370-3141309cb7OI"
    })
    public void deleta_consumerIdInvalido(final String consumerId) throws Exception {
        final var request = new AdicionaItemRequestDTO(
                consumerId.equals("-1") ? null : consumerId,
                123456L,
                1L
        );
        final var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        final var jsonRequest = objectMapper.writeValueAsString(request);

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.post(URL_ITEMS)
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

}
