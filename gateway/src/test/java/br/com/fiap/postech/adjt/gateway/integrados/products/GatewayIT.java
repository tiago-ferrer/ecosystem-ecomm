package br.com.fiap.postech.adjt.gateway.integrados.products;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockserver.model.MediaType.APPLICATION_JSON;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GatewayIT {

    private WebTestClient client;

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        this.client = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    public void busca() throws Exception {
        final var clientAndServer = ClientAndServer.startClientAndServer(8083);
        clientAndServer.when(
                        HttpRequest.request()
                                .withMethod("GET")
                                .withPath("/item/1/1")
                )
                .respond(
                        HttpResponse.response()
                                .withContentType(APPLICATION_JSON)
                                .withStatusCode(200)
                                .withBody("""
                                        {
                                            "id": 1,
                                            "title": "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
                                            "price": 109.95
                                        }
                                        """)
                );

        this.client.method(HttpMethod.GET)
                .uri("/products/item/1/1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Map.class)
                .consumeWith(result -> {
                    assertThat(result.getResponseBody()).isNotEmpty();
                });

        clientAndServer.stop();
    }

}
