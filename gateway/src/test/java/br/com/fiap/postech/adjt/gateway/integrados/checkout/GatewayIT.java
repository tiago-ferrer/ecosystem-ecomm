package br.com.fiap.postech.adjt.gateway.integrados.checkout;

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
    public void processa() throws Exception {
        final var clientAndServer = ClientAndServer.startClientAndServer(8082);
        clientAndServer.when(
                        HttpRequest.request()
                                .withMethod("POST")
                                .withPath("/")
                )
                .respond(
                        HttpResponse.response()
                                .withContentType(APPLICATION_JSON)
                                .withStatusCode(200)
                                .withBody("""
                                        {
                                            "orderId": "e7c5c208-c4c3-42fc-9370-3141309cb7bc",
                                            "status": "pending"
                                        }
                                        """)
                );

        this.client.method(HttpMethod.POST)
                .uri("/checkout/")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Map.class)
                .consumeWith(result -> {
                    assertThat(result.getResponseBody()).isNotEmpty();
                });

        clientAndServer.stop();
    }

    @Test
    public void busca() throws Exception {
        final var clientAndServer = ClientAndServer.startClientAndServer(8082);
        clientAndServer.when(
                        HttpRequest.request()
                                .withMethod("GET")
                                .withPath("/e7c5c208-c4c3-42fc-9370-3141309cb7bc")
                )
                .respond(
                        HttpResponse.response()
                                .withContentType(APPLICATION_JSON)
                                .withStatusCode(200)
                                .withBody("""
                                        {
                                            "orders": [
                                                {
                                                    "orderId": "e7c5c208-c4c3-42fc-9370-3141309cb7bc",
                                                    "items": [
                                                        {
                                                            "itemId": 1,
                                                            "quantity": 3
                                                        }
                                                    ],
                                                    "paymentType": "cartao",
                                                    "value": 100.0,
                                                    "status": "pending"
                                                }
                                            ]
                                        }
                                        """)
                );

        this.client.method(HttpMethod.GET)
                .uri("/checkout/e7c5c208-c4c3-42fc-9370-3141309cb7bc")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Map.class)
                .consumeWith(result -> {
                    assertThat(result.getResponseBody()).isNotEmpty();
                });

        clientAndServer.stop();
    }

    @Test
    public void buscaPorOrderId() throws Exception {
        final var clientAndServer = ClientAndServer.startClientAndServer(8082);
        clientAndServer.when(
                        HttpRequest.request()
                                .withMethod("GET")
                                .withPath("/by-order-id/e7c5c208-c4c3-42fc-9370-3141309cb7bc")
                )
                .respond(
                        HttpResponse.response()
                                .withContentType(APPLICATION_JSON)
                                .withStatusCode(200)
                                .withBody("""
                                        {
                                            "orders":
                                                {
                                                    "orderId": "e7c5c208-c4c3-42fc-9370-3141309cb7bc",
                                                    "items": [
                                                        {
                                                            "itemId": 1,
                                                            "quantity": 3
                                                        }
                                                    ],
                                                    "paymentType": "cartao",
                                                    "value": 100.0,
                                                    "status": "pending"
                                                }
                                        }
                                        """)
                );

        this.client.method(HttpMethod.GET)
                .uri("/checkout/by-order-id/e7c5c208-c4c3-42fc-9370-3141309cb7bc")
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
