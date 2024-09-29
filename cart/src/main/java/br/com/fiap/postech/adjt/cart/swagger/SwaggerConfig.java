package br.com.fiap.postech.adjt.cart.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Value;

@OpenAPIDefinition(
        info = @Info(
                title = "Carrinho de Compras",
                version = "1.0",
                description = "Microsserviço destinado à administração de carrinhos de compras.",
                contact = @Contact(name = "E3gSIX", url = "https://github.com/E3gSIX")
        )
)

public class SwaggerConfig {
    @Value("${springdoc.swagger-ui.path}")
    public static final String URL_SWAGGER = "/doc";
}
