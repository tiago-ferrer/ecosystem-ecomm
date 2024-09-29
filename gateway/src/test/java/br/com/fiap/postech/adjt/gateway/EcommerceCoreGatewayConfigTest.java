package br.com.fiap.postech.adjt.gateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@ActiveProfiles("local")
@SpringBootTest
public class EcommerceCoreGatewayConfigTest {

    @Autowired
    private EcommerceCoreGatewayConfig config;

    @Test
    void testInstance() throws Exception {
        RouterFunction<ServerResponse> resp = config.gatewayRouterFunctionsAddReqHeader();
        Assertions.assertInstanceOf(RouterFunction.class, resp);
    }

}
