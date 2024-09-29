package br.com.fiap.postech.adjt.gateway.integrados.cart;

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
    public void adiciona() throws Exception {
        final var clientAndServer = ClientAndServer.startClientAndServer(8081);
        clientAndServer.when(
                        HttpRequest.request()
                                .withMethod("POST")
                                .withPath("/items")
                )
                .respond(
                        HttpResponse.response()
                                .withContentType(APPLICATION_JSON)
                                .withStatusCode(200)
                                .withBody("""
                                        {
                                            "message": "Item added to cart successfully"
                                        }
                                        """)
                );

        final var request = """
                {
                    "consumerId": "e7c5c208-c4c3-42fc-9370-3141309cb7d1",
                    "itemId": 123456,
                    "quantity": 1
                }
                """;
        final var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        final var jsonRequest = objectMapper.writeValueAsString(request);

        this.client.post()
                .uri("/cart/items")
                .bodyValue(jsonRequest)
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
    public void deleta() throws Exception {
        final var clientAndServer = ClientAndServer.startClientAndServer(8081);
        clientAndServer.when(
                        HttpRequest.request()
                                .withMethod("DELETE")
                                .withPath("/item")
                )
                .respond(
                        HttpResponse.response()
                                .withContentType(APPLICATION_JSON)
                                .withStatusCode(200)
                                .withBody("""
                                        {
                                            "message": "Item removed from cart successfully"
                                        }
                                        """)
                );

        final var request = """
                {
                    "consumerId": "e7c5c208-c4c3-42fc-9370-3141309cb7d1",
                    "itemId": 123456
                }
                """;
        final var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        final var jsonRequest = objectMapper.writeValueAsString(request);

        this.client.method(HttpMethod.DELETE)
                .uri("/cart/item")
                .bodyValue(jsonRequest)
                .exchange()
                .expectStatus()
                .isNotFound();

        clientAndServer.stop();
    }

    @Test
    public void atualiza() throws Exception {
        final var clientAndServer = ClientAndServer.startClientAndServer(8081);
        clientAndServer.when(
                        HttpRequest.request()
                                .withMethod("PUT")
                                .withPath("/item")
                )
                .respond(
                        HttpResponse.response()
                                .withContentType(APPLICATION_JSON)
                                .withStatusCode(200)
                                .withBody("""
                                        {
                                            "message": "Item updated from cart successfully"
                                        }
                                        """)
                );

        final var request = """
                {
                    "consumerId": "e7c5c208-c4c3-42fc-9370-3141309cb7d1",
                    "itemId": 123456
                }
                """;
        final var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        final var jsonRequest = objectMapper.writeValueAsString(request);

        this.client.put()
                .uri("/cart/item")
                .bodyValue(jsonRequest)
                .exchange()
                .expectStatus()
                .isOk();

        clientAndServer.stop();
    }

    @Test
    public void deletaOCarrinho() throws Exception {
        final var clientAndServer = ClientAndServer.startClientAndServer(8081);
        clientAndServer.when(
                        HttpRequest.request()
                                .withMethod("DELETE")
                                .withPath("/")
                )
                .respond(
                        HttpResponse.response()
                                .withContentType(APPLICATION_JSON)
                                .withStatusCode(200)
                                .withBody("""
                                        {
                                            "message": "Item updated from cart successfully"
                                        }
                                        """)
                );

        final var request = """
                {
                    "consumerId": "e7c5c208-c4c3-42fc-9370-3141309cb7d1"
                }
                """;
        final var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        final var jsonRequest = objectMapper.writeValueAsString(request);

        this.client.method(HttpMethod.DELETE)
                .uri("/cart/")
                .bodyValue(jsonRequest)
                .exchange()
                .expectStatus()
                .isNotFound();

        clientAndServer.stop();
    }

    @Test
    public void busca() throws Exception {
        final var clientAndServer = ClientAndServer.startClientAndServer(8081);
        clientAndServer.when(
                        HttpRequest.request()
                                .withMethod("GET")
                                .withPath("/")
                )
                .respond(
                        HttpResponse.response()
                                .withContentType(APPLICATION_JSON)
                                .withStatusCode(200)
                                .withBody("""
                                        {
                                           "items": [
                                             {
                                               "itemId": 1,
                                               "qnt": 1
                                             }
                                           ]
                                         }
                                        
                                        """)
                );

        final var request = """
                {
                    "consumerId": "e7c5c208-c4c3-42fc-9370-3141309cb7d1"
                }
                """;
        final var objectMapper = this.objectMapper
                .writer()
                .withDefaultPrettyPrinter();
        final var jsonRequest = objectMapper.writeValueAsString(request);

        this.client.method(HttpMethod.GET)
                .uri("/cart/")
                .bodyValue(jsonRequest)
                .exchange()
                .expectStatus()
                .isOk();

        clientAndServer.stop();
    }

}
