package br.com.fiap.postech.adjt.gateway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@WebMvcTest
public class GatewayIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WebClient.Builder webClientBuilder;

    private WebClient webClient;

    @BeforeEach
    void setUp() {
        webClient = WebClient.create();
        when(webClientBuilder.build()).thenReturn(webClient);
    }

    @Test
    void testProductsRoute() throws Exception {
        String responseBody = "{\"products\":[{\"id\":1,\"title\":\"Test Product\"}]}";

        when(webClient.get().uri("https://fakestoreapi.com/products/test").retrieve().bodyToMono(String.class))
                .thenReturn(Mono.just(responseBody));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/products/test")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(responseBody));
    }

    @Test
    void testTimeoutHandling() throws Exception {
        when(webClient.get().uri("https://fakestoreapi.com/products/test").retrieve().bodyToMono(String.class))
                .thenThrow(new RuntimeException("Timeout occurred"));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/products/test")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}
