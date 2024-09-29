package br.com.fiap.postech.adjt.checkout.dataprovider.integration.config;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WebClientConfigTests {

    private final WebClientConfig webClientConfig = new WebClientConfig();

    @Test
    void testWebClientPayment() {
        // Arrange
        WebClient.Builder builder = mock(WebClient.Builder.class);
        WebClient webClient = mock(WebClient.class);

        // Act
        when(builder.baseUrl("https://payment-api-latest.onrender.com")).thenReturn(builder);
        when(builder.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)).thenReturn(builder);
        when(builder.defaultHeader("apiKey", "colocar-key-grup")).thenReturn(builder);
        when(builder.build()).thenReturn(webClient);

        WebClient result = webClientConfig.webClientPayment(builder);

        // Assert
        assertNotNull(result);
        Mockito.verify(builder).baseUrl("https://payment-api-latest.onrender.com");
        Mockito.verify(builder).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        Mockito.verify(builder).defaultHeader("apiKey", "colocar-key-grup");
        Mockito.verify(builder).build();
    }

    @Test
    void testWebClientCart() {
        // Arrange
        WebClient.Builder builder = mock(WebClient.Builder.class);
        WebClient webClient = mock(WebClient.class);

        // Act
        when(builder.baseUrl("http://cart:8081")).thenReturn(builder);
        when(builder.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)).thenReturn(builder);
        when(builder.build()).thenReturn(webClient);

        WebClient result = webClientConfig.webClientCart(builder);

        // Assert
        assertNotNull(result);
        Mockito.verify(builder).baseUrl("http://cart:8081");
        Mockito.verify(builder).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        Mockito.verify(builder).build();
    }
}
