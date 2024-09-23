package br.com.fiap.postech.adjt.products.integrados;

import br.com.fiap.postech.adjt.products.infrastructure.fakeStoreApi.client.FakeStoreApiClient;
import br.com.fiap.postech.adjt.products.infrastructure.fakeStoreApi.client.response.ItemDTO;
import br.com.fiap.postech.adjt.products.infrastructure.products.controller.dto.ErrorHandlingResponseDTO;
import br.com.fiap.postech.adjt.products.infrastructure.products.controller.dto.ItemResponseDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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

import static br.com.fiap.postech.adjt.products.infrastructure.products.controller.ProductsController.URL_ITEM;


@AutoConfigureMockMvc
@SpringBootTest
public class ProductsControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @MockBean
    FakeStoreApiClient fakeStoreApiClient;

    @Test
    public void busca_deveRetornar200() throws Exception {
        Mockito.when(this.fakeStoreApiClient.pegaItem(1L))
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

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.get(URL_ITEM
                        .replace("{itemId}", "1")
                        .replace("{quantity}", "1")
                )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk()
                )
                .andReturn();

        final var responseAppString = response.getResponse().getContentAsString();
        final var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<ItemResponseDTO>() {});

        Assertions.assertEquals(1L, responseApp.id());
        Assertions.assertEquals("Item Teste", responseApp.title());
        Assertions.assertEquals(new BigDecimal("100"), responseApp.price());
    }

    @Test
    public void busca_deveRetornar400_itemNaoExiste() throws Exception {
        Mockito.when(this.fakeStoreApiClient.pegaItem(1L))
                .thenReturn(
                       null
                );

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.get(URL_ITEM
                                .replace("{itemId}", "1")
                                .replace("{quantity}", "1")
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest()
                )
                .andReturn();

        final var responseAppString = response.getResponse().getContentAsString();
        final var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<ErrorHandlingResponseDTO>() {});

        Assertions.assertEquals(1L, responseApp.codeError());
        Assertions.assertEquals("Item nao encontrado", responseApp.description());
    }

    @Test
    public void busca_deveRetornar400_indisponibilidadeNaApiExterna() throws Exception {
        Mockito.doThrow(
                        new IllegalArgumentException("API EXTERNA COM PROBLEMAS!")
                )
                .when(this.fakeStoreApiClient)
                .pegaItem(1L);

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.get(URL_ITEM
                                .replace("{itemId}", "1")
                                .replace("{quantity}", "1")
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest()
                )
                .andReturn();

        final var responseAppString = response.getResponse().getContentAsString();
        final var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<ErrorHandlingResponseDTO>() {});

        Assertions.assertEquals(0L, responseApp.codeError());
        Assertions.assertEquals("ERRO NAO MAPEADO, ENTRAR EM CONTATO COM O SUPORTE", responseApp.description());
    }

    @ParameterizedTest
    @ValueSource(longs = {
            -2,
            0
    })
    public void busca_deveRetornar400_campoQuantityInvalido(final Long quantity) throws Exception {
        Mockito.when(this.fakeStoreApiClient.pegaItem(1L))
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

        final var response = this.mockMvc
                .perform(MockMvcRequestBuilders.get(URL_ITEM
                                .replace("{itemId}", "1")
                                .replace("{quantity}", quantity.toString())
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest()
                )
                .andReturn();

        final var responseAppString = response.getResponse().getContentAsString();
        final var responseApp = this.objectMapper
                .readValue(responseAppString, new TypeReference<ErrorHandlingResponseDTO>() {});

        Assertions.assertEquals(2L, responseApp.codeError());
        Assertions.assertEquals("Quantidade invalida", responseApp.description());
    }

}
