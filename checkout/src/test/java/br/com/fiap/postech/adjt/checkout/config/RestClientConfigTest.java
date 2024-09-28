package br.com.fiap.postech.adjt.checkout.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RestClientConfigTest {

    @Test
    public void testRestClientBeanCreation() {
        // Cria um contexto de aplicação baseado na configuração
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RestClientConfig.class);

        // Obtém o bean RestClient do contexto
        RestClient restClient = context.getBean(RestClient.class);

        // Verifica se o bean foi criado corretamente
        assertNotNull(restClient, "O bean RestClient não deveria ser nulo");

        // Fecha o contexto
        context.close();
    }
}