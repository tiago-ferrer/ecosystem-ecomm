package br.com.fiap.postech.adjt.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class GatewayApplicationTests {

    @Autowired
    private EcommerceCoreGatewayConfig config;

    @Test
    void contextLoads() {
        GatewayApplication.main(new String[]{});
        assertNotNull(config);
    }

}
