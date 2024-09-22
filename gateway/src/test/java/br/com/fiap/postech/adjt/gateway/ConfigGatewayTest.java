package br.com.fiap.postech.adjt.gateway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConfigGatewayTest {

    @InjectMocks
    private ConfigGateway configGateway;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRestTemplate() {
        RestTemplate restTemplate = configGateway.restTemplate();
        assertNotNull(restTemplate);
    }
}
